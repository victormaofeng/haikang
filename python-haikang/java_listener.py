import json
import pika
import algo
import util

"""
/**
 * 目标检测或重识别时java和python通信类
 *
 * @author 毛逢
 * @date 2021/9/18 9:49
 */
@Data
public class DetectionMessage {
    // 文件根路径
    private String rootPath;

    private int detectId;
    
    
    // 重识别时需要识别的对象的图片
    private String path;
    private String type;

    // 待检测的文件位置(相对地址)
    private String detectPath;
    private String detectType;

    // 检测完毕后生成的新文件存放位置(相对地址)
    private String detectedPath;
    private String detectedType;

    // 使用算法
    private int algoId;
}

java端和python目标检测的通信类
"""

# 队列名由java端指定，不可更改

# 目标检测队列
JAVA_DETECTION_QUEUE = "detection_queue"

# 行人重识别队列
JAVA_REID_QUEUE = "reid_queue"

credentials = pika.PlainCredentials('guest', 'guest')

PYTHON_DETECTION_REPLY_QUEUE = 'python_detection_reply_queue'

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost', port=5672, virtual_host='/', credentials=credentials))

channel = connection.channel()

channel.queue_declare(queue=PYTHON_DETECTION_REPLY_QUEUE, durable=True)

# 目标检测队列声明
channel.queue_declare(queue=JAVA_DETECTION_QUEUE, durable=True)

channel.queue_declare(queue=PYTHON_DETECTION_REPLY_QUEUE, durable=True)


# 定义一个回调函数来处理消息队列中的消息，这里是打印出来
def detection_callback(ch, method, properties, byte_msg):
    ch.basic_ack(delivery_tag=method.delivery_tag)
    # 处理java信息,转成字典处理
    print(byte_msg.decode("utf-8"))
    detection_message = json.loads(byte_msg.decode("utf-8"))

    source_file = util.concat_path(detection_message.rootPath, detection_message.detectPath)
    dest_file = util.concat_path(detection_message.rootPath, detection_message.detectedPath)
    process = algo.get_algo(detection_message.algoId)
    # 没有目标行人图片,则是检测任务
    if detection_message.path is None or detection_message.path == "":
        algo.detect(source_file, dest_file, process)
    # 有目标行人图片,则是重识别任务
    else:
        person_img = util.concat_path(detection_message.rootPath, detection_message.path)
        algo.re_id(person_img, source_file, dest_file, process)
    channel.basic_publish(exchange='', routing_key=PYTHON_DETECTION_REPLY_QUEUE, body=byte_msg.decode("utf-8"))


# 告诉rabbitmq，用callback来接收消息
channel.basic_consume(JAVA_DETECTION_QUEUE, detection_callback)

# 开始接收信息，并进入阻塞状态，队列里有信息才会调用callback进行处理
channel.start_consuming()
