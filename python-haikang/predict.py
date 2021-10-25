'''
predict.py有几个注意点
1、该代码无法直接进行批量预测，如果想要批量预测，可以利用os.listdir()遍历文件夹，利用Image.open打开图片文件进行预测。
具体流程可以参考get_dr_txt.py，在get_dr_txt.py即实现了遍历还实现了目标信息的保存。
2、如果想要进行检测完的图片的保存，利用r_image.save("img.jpg")即可保存，直接在predict.py里进行修改即可。 
3、如果想要获得预测框的坐标，可以进入yolo.detect_image函数，在绘图部分读取top，left，bottom，right这四个值。
4、如果想要利用预测框截取下目标，可以进入yolo.detect_image函数，在绘图部分利用获取到的top，left，bottom，right这四个值
在原图上利用矩阵的方式进行截取。
5、如果想要在预测图上写额外的字，比如检测到的特定目标的数量，可以进入yolo.detect_image函数，在绘图部分对predicted_class进行判断，
比如判断if predicted_class == 'car': 即可判断当前目标是否为车，然后记录数量即可。利用draw.text即可写字。
'''
from PIL import Image
from yolo import YOLO
import base
import numpy

yolo = YOLO()


class Yolo3Processor(base.Processor):
    def process(self, frame):
        img = yolo.detect_image(Image.fromarray(frame))
        return numpy.array(img)


if __name__ == "__main__":
    yolo3_processor = Yolo3Processor()
    push = base.PushStream(
        pull_rtmp_url="rtmp://rtmp01open.ys7.com:1935/v3/openlive/F02718370_1_1?expire=1620264299&id=309983811356180480&t=fd01306f6521a7487c5c27382145d4506f1b8b7877550bace7009d27922417de&ev=100",
        push_rtmp_url="rtmp://0.0.0.0:1935/stream/123", processor=yolo3_processor)
    push.run()

# while True:
#     img = input('Input image filename:')
#     try:
#         image = Image.open(img)
#     except:
#         print('Open Error! Try again!')
#         continue
#     else:
#         r_image = yolo.detect_image(image)
#         r_image.show()
