# from yolo import YOLO
import cv2
from PIL import Image
import numpy
import subprocess as sp


"""
算法模块
用以定义所有的算法
"""


# 接口  处理拉流帧,进行目标检测等操作
class Processor:
    # 实际处理函数,对拉流的帧进行处理并返回处理后的帧
    # 目标检测方法
    def process(self, frame: numpy.ndarray):
        pass

    # 重识别方法
    def recognize(self, source_frame: numpy.ndarray, dest_frame: numpy.ndarray):
        pass


# 目标检测父类
class DetectionProcessor(Processor):
    def process(self, frame: numpy.ndarray):
        return frame


# 行人重识别父类
class ReIdProcessor(Processor):
    def recognize(self, source_frame: numpy.ndarray, dest_frame: numpy.ndarray):
        return dest_frame


# yolo3
class Yolo3DetectionProcessor(DetectionProcessor):
    def __init__(self):
        # self.yolo = YOLO()
        self.yolo = None

    def process(self, frame: numpy.ndarray):
        img = self.yolo.detect_image(Image.fromarray(frame))
        return numpy.array(img)