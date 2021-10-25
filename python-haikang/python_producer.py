import pika
import json


"""
/**
 * 目标检测时java和python通信类
 *
 * @author 毛逢
 * @date 2021/9/18 9:49
 */
@Data
public class DetectionMessage {
    // 文件根路径
    private String rootPath;

    private int detectId;

    // 待检测的文件位置(相对地址)
    private String detectPath;
    private String detectType;

    // 检测完毕后生成的新文件存放位置(相对地址)
    private String detectedPath;
    private String detectedType;

    // 使用算法
    private int algoId;
}

python处理完java的消息，回复java用的
"""
PYTHON_DETECTION_REPLY_QUEUE = 'python_detection_reply_queue'

credentials = pika.PlainCredentials('guest', 'guest')  # mq用户名和密码
# 虚拟队列需要指定参数 virtual_host，如果是默认的可以不填。
connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='58.155.40.70', port=5672, virtual_host='/', credentials=credentials))
channel = connection.channel()
# 声明消息队列，消息将在这个队列传递，如不存在，则创建
channel.queue_declare(queue=PYTHON_DETECTION_REPLY_QUEUE, durable=True)


def detection_reply(message):
    # 向队列插入数值 routing_key是队列名
    channel.basic_publish(exchange='', routing_key=PYTHON_DETECTION_REPLY_QUEUE, body=message)
    print(message)
    # connection.close()

# channel.basic_publish(exchange='', routing_key=PYTHON_DETECTION_REPLY_QUEUE, body="haha")
