"""
工具模块
"""

import algo
import cv2
from PIL import Image
import numpy
import subprocess as sp
import multiprocessing

import search3
import search2

"""
拼接文件路径
"""


def concat_path(parent: str, child: str):
    # linux格式
    if parent.find("/") > -1:
        if parent.endswith("/") and child.startswith("/"):
            return parent + child.replace('/', '', 1)
        elif (not parent.endswith("/")) and not child.startswith("/"):
            return parent + "/" + child
        else:
            return parent + child
    # windows格式
    else:
        if parent.endswith("\\") and child.startswith("/"):
            return parent + child.replace('/', '', 1).replace('/', '\\')
        elif (not parent.endswith('\\')) and not child.startswith("/"):
            return parent + "\\" + child.replace('/', '\\')
        else:
            return parent + child.replace('/', '\\')


"""
根据算法id,获取对应算法对象
"""


def get_algo(algo_id):
    if algo_id == 1:
        return search3.DarknetDetectionProcessor()

    elif algo_id == 2:
        return search2.ResReIdProcessor()


"""
对图片或视频文件进行目标检测
source_file: str 源文件
dest_file: str 目标文件
detection_process: DetectionProcessor 目标检测算法
注意,此处文件皆为完整的绝对路径
视频转换采用cv22,cv22通过拼接frame形成的视频文件,前端播放器无法读取播放
所以该方法不可用
"""


def detect(source_file: str, dest_file: str, detection_process: algo.Processor):
    # 源文件为图片
    if source_file.endswith('png') or source_file.endswith('jpg') or source_file.endswith('jpeg'):
        img = cv2.imread(source_file, cv2.IMREAD_GRAYSCALE)
        img = detection_process.process(img)
        cv2.imwrite(dest_file, img)
        return

    source_cap = None
    dest_cap = None
    try:
        # 源文件为视频
        source_cap = cv2.VideoCapture(source_file)
        # 获取视频宽度
        frame_width = int(source_cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        # 获取视频高度
        frame_height = int(source_cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        # 视频平均帧率
        fps = source_cap.get(cv2.CAP_PROP_FPS)

        # 视频格式不同,写入的模式也不同
        if dest_file.endswith("flv"):
            fourcc = cv2.VideoWriter_fourcc('F', 'L', 'V', '1')
        elif dest_file.endswith("mp4"):
            fourcc = cv2.VideoWriter_fourcc(*'mp4v')
        elif dest_file.endswith("avi"):
            fourcc = cv2.VideoWriter_fourcc(*'XVID')
        else:
            fourcc = cv2.VideoWriter_fourcc()

        dest_cap = cv2.VideoWriter(dest_file, fourcc, fps, (frame_width, frame_height))

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


def detect2(source_file: str, dest_file: str, detection_process: algo.Processor):
    # 源文件为图片
    if source_file.endswith('png') or source_file.endswith('jpg') or source_file.endswith('jpeg'):
        img = cv2.imread(source_file, cv2.IMREAD_GRAYSCALE)
        img = detection_process.process(img)
        cv2.imwrite(dest_file, img)
        return

    # 帧队列,用于保存帧
    # frame_queue = multiprocessing.Queue()

    source_cap = None

    # 源文件为视频
    source_cap = cv2.VideoCapture(source_file)
    # 获取视频宽度
    frame_width = int(source_cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    # 获取视频高度
    frame_height = int(source_cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    # 视频平均帧率
    fps = source_cap.get(cv2.CAP_PROP_FPS)

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
    i = 0



    # push_frame_thread = multiprocessing.Process(target=, args=())
    # push_frame_thread.daemon = True  # 把子进程设置为daemon方式
    # push_frame_thread.start()  # 运行子进程

    while is_open:
        # 获取帧
        ret, frame = source_cap.read()
        if not ret or frame is None:
            p.stdin.flush()
            break

        # frame_queue.put(frame)

        frame2 = detection_process.process(frame)

        i = i + 1
        print("=========" + str(i) + "============")
        p.stdin.write(frame2)





"""
person_img: str, 行人图片
source_file: str,待识别图片或视频
dest_file: str,已识别图片或视频
detection_process: ReIdProcessor 行人重识别算法类对象
"""


def re_id(person_img: str, source_file: str, dest_file: str, detection_process: algo.ReIdProcessor):
    if person_img.endswith('png') is False or person_img.endswith('jpg') is False or person_img.endswith(
            'jpeg') is False:
        return

    person_frame = cv2.imread(person_img, cv2.IMREAD_GRAYSCALE)

    # 源文件为图片,直接识别
    if source_file.endswith('png') or source_file.endswith('jpg') or source_file.endswith('jpeg'):
        img = cv2.imread(source_file, cv2.IMREAD_GRAYSCALE)
        img = detection_process.recognize(person_frame, img)
        cv2.imwrite(dest_file, img)
        return

    source_cap = None
    try:
        # 源文件为视频
        source_cap = cv2.VideoCapture(source_file)
        # 获取视频宽度
        frame_width = int(source_cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        # 获取视频高度
        frame_height = int(source_cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        # 视频平均帧率
        fps = source_cap.get(cv2.CAP_PROP_FPS)

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
