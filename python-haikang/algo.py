import cv2 as cv
# from yolo import YOLO
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
    def process(self, frame):
        pass

    # 重识别方法
    def recognize(self, source_frame, dest_frame):
        pass


# 目标检测父类
class DetectionProcessor(Processor):
    def process(self, frame):
        return frame


# 行人重识别父类
class ReIdProcessor(Processor):
    def recognize(self, source_frame, dest_frame):
        return dest_frame


# yolo3
class Yolo3Processor(DetectionProcessor):
    def __init__(self):
        # self.yolo = YOLO()
        self.yolo = None

    def process(self, frame):
        img = self.yolo.detect_image(Image.fromarray(frame))
        return numpy.array(img)


"""
对图片或视频文件进行目标检测
source_file: str 源文件
dest_file: str 目标文件
detection_process: DetectionProcessor 目标检测算法
注意,此处文件皆为完整的绝对路径
视频转换采用cv2,cv2通过拼接frame形成的视频文件,前端播放器无法读取播放
所以该方法不可用
"""


def detect(source_file: str, dest_file: str, detection_process: Processor):
    # 源文件为图片
    if source_file.endswith('png') or source_file.endswith('jpg') or source_file.endswith('jpeg'):
        img = cv.imread(source_file, cv.IMREAD_GRAYSCALE)
        img = detection_process.process(img)
        cv.imwrite(dest_file, img)
        return

    source_cap = None
    dest_cap = None
    try:
        # 源文件为视频
        source_cap = cv.VideoCapture(source_file)
        # 获取视频宽度
        frame_width = int(source_cap.get(cv.CAP_PROP_FRAME_WIDTH))
        # 获取视频高度
        frame_height = int(source_cap.get(cv.CAP_PROP_FRAME_HEIGHT))
        # 视频平均帧率
        fps = source_cap.get(cv.CAP_PROP_FPS)

        # 视频格式不同,写入的模式也不同
        if dest_file.endswith("flv"):
            fourcc = cv.VideoWriter_fourcc('F', 'L', 'V', '1')
        elif dest_file.endswith("mp4"):
            fourcc = cv.VideoWriter_fourcc(*'mp4v')
        elif dest_file.endswith("avi"):
            fourcc = cv.VideoWriter_fourcc(*'XVID')
        else:
            fourcc = cv.VideoWriter_fourcc()

        dest_cap = cv.VideoWriter(dest_file, fourcc, fps, (frame_width, frame_height))

        is_open = source_cap.isOpened()

        while is_open:
            # 获取帧
            ret, frame = source_cap.read()
            if not ret or frame is None:
                break
            frame = detection_process.process(frame)
            dest_cap.write(frame)
    except BaseException:
        pass
    finally:
        if dest_cap is not None:
            dest_cap.release()
        if source_cap is not None:
            source_cap.release()


"""
对图片或视频文件进行目标检测
source_file: str 源文件
dest_file: str 目标文件
detection_process: DetectionProcessor 目标检测算法
注意,此处文件皆为完整的绝对路径
视频转换采用ffmpeg
"""


def detect2(source_file: str, dest_file: str, detection_process: Processor):
    # 源文件为图片
    if source_file.endswith('png') or source_file.endswith('jpg') or source_file.endswith('jpeg'):
        img = cv.imread(source_file, cv.IMREAD_GRAYSCALE)
        img = detection_process.process(img)
        cv.imwrite(dest_file, img)
        return

    source_cap = None
    try:
        # 源文件为视频
        source_cap = cv.VideoCapture(source_file)
        # 获取视频宽度
        frame_width = int(source_cap.get(cv.CAP_PROP_FRAME_WIDTH))
        # 获取视频高度
        frame_height = int(source_cap.get(cv.CAP_PROP_FRAME_HEIGHT))
        # 视频平均帧率
        fps = source_cap.get(cv.CAP_PROP_FPS)

        # 初始化推流命令
        command = ['ffmpeg',
                   '-y',
                   '-f', 'rawvideo',
                   '-vcodec', 'rawvideo',
                   '-pix_fmt', 'bgr24',
                   '-s', "{}*{}".format(frame_width, frame_height),
                   '-r', str(fps),
                   '-i', '-',
                   '-c:v', 'libx264',
                   '-pix_fmt', 'yuv420p',
                   '-preset', 'ultrafast',
                   '-f', 'flv',
                   dest_file]

        is_open = source_cap.isOpened()
        p = sp.Popen(command, stdin=sp.PIPE)
        while is_open:
            # 获取帧
            ret, frame = source_cap.read()
            if not ret or frame is None:
                p.stdin.flush()
                break
            frame = detection_process.process(frame)
            p.stdin.write(frame.tostring())

    except BaseException:
        pass

    finally:
        if source_cap is not None:
            source_cap.release()


"""
person_img: str, 行人图片
source_file: str,待识别图片或视频
dest_file: str,已识别图片或视频
detection_process: ReIdProcessor 行人重识别算法类对象
"""


def re_id(person_img: str, source_file: str, dest_file: str, detection_process: ReIdProcessor):
    if person_img.endswith('png') is False or person_img.endswith('jpg') is False or person_img.endswith(
            'jpeg') is False:
        return

    person_frame = cv.imread(person_img, cv.IMREAD_GRAYSCALE)

    # 源文件为图片,直接识别
    if source_file.endswith('png') or source_file.endswith('jpg') or source_file.endswith('jpeg'):
        img = cv.imread(source_file, cv.IMREAD_GRAYSCALE)
        img = detection_process.recognize(person_frame, img)
        cv.imwrite(dest_file, img)
        return

    source_cap = None
    try:
        # 源文件为视频
        source_cap = cv.VideoCapture(source_file)
        # 获取视频宽度
        frame_width = int(source_cap.get(cv.CAP_PROP_FRAME_WIDTH))
        # 获取视频高度
        frame_height = int(source_cap.get(cv.CAP_PROP_FRAME_HEIGHT))
        # 视频平均帧率
        fps = source_cap.get(cv.CAP_PROP_FPS)

        # 初始化推流命令
        command = ['ffmpeg',
                   '-y',
                   '-f', 'rawvideo',
                   '-vcodec', 'rawvideo',
                   '-pix_fmt', 'bgr24',
                   '-s', "{}*{}".format(frame_width, frame_height),
                   '-r', str(fps),
                   '-i', '-',
                   '-c:v', 'libx264',
                   '-pix_fmt', 'yuv420p',
                   '-preset', 'ultrafast',
                   '-f', 'flv',
                   dest_file]

        is_open = source_cap.isOpened()
        p = sp.Popen(command, stdin=sp.PIPE)
        while is_open:
            # 获取帧
            ret, frame = source_cap.read()
            if not ret or frame is None:
                p.stdin.flush()
                break
            frame = detection_process.recognize(person_frame, frame)
            p.stdin.write(frame.tostring())

    except BaseException:
        pass

    finally:
        if source_cap is not None:
            source_cap.release()


"""
根据算法id,获取对应算法对象
"""


def get_algo(algo_id):
    if algo_id == 1:
        return DetectionProcessor()
    elif algo_id == 2:
        return ReIdProcessor()

# detect2("C:\\Users\\Administrator\\Desktop\\1\\11.mp4", "C:\\Users\\Administrator\\Desktop\\1\\13.mp4", DetectionProcessor())
