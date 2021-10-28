import cv2 as cv
import time
import subprocess as sp
import multiprocessing

"""
该模块废弃，暂不使用
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


# 实现类,目标检测
class DetectionProcessor(Processor):
    def process(self, frame):
        return frame


# 废弃
class PushStream:
    def __init__(self, pull_rtsp_url=None, pull_rtmp_url=None, push_rtmp_url=None, processor=None):
        # 拉流地址  摄像头与服务器在同一局域网下可用
        self.pull_rtsp_url = pull_rtsp_url
        # 拉流地址 摄像头与服务器不在同一局域网下使用
        self.pull_rtmp_url = pull_rtmp_url
        # 推流地址
        self.push_rtmp_url = push_rtmp_url
        # 处理拉流帧的对象
        self.processor = processor
        # 帧队列,用于保存帧
        self.frame_queue = multiprocessing.Queue()

    # 拉流
    def pull_stream(self):
        # 优先使用rtsp流,减少视频延迟
        if self.pull_rtsp_url is not None:
            cap = cv.VideoCapture(self.pull_rtsp_url)
        else:
            cap = cv.VideoCapture(self.pull_rtmp_url)

        while cap.isOpened():
            ret, frame = cap.read()
            # 如果队列没满且拉取帧正常
            if not self.frame_queue.full():
                # print("生产帧")
                # 对获取的帧进行画面处理
                # new_frame = self.processor.process(frame)
                # self.frame_queue.put(new_frame)  # 送入队列
                if ret:
                    self.frame_queue.put(frame)
            # cv.waitKey(1)
        # cap.release()

    # 推流
    def push_stream(self):
        # 优先使用rtsp流,减少视频延迟
        if self.pull_rtsp_url is not None:
            cap = cv.VideoCapture(self.pull_rtsp_url)
        else:
            cap = cv.VideoCapture(self.pull_rtmp_url)

        # 获取视频宽度
        frame_width = int(cap.get(cv.CAP_PROP_FRAME_WIDTH))
        # 获取视频高度
        frame_height = int(cap.get(cv.CAP_PROP_FRAME_HEIGHT))
        # 视频平均帧率
        fps = cap.get(cv.CAP_PROP_FPS)

        print("帧率:" + str(fps))
        # 初始化推流命令
        command = ['ffmpeg',
                   '-y',
                   '-f', 'rawvideo',
                   '-vcodec', 'rawvideo',
                   '-pix_fmt', 'bgr24',
                   '-s', "{}*{}".format(frame_width, frame_height),
                   '-r', str(fps / 5),
                   '-i', '-',
                   '-c:v', 'libx264',
                   '-pix_fmt', 'yuv420p',
                   '-preset', 'ultrafast',
                   '-f', 'flv',
                   self.push_rtmp_url]
        # 释放内存
        cap.release()
        p = sp.Popen(command, stdin=sp.PIPE)
        fps = int(fps / 5)
        i = 0
        while True:
            if not self.frame_queue.empty():  # 如果输入管道不为空
                print("消费帧")
                # 把帧和相关信息从输入队列中取出
                frame = self.frame_queue.get()
                i = (i + 1) % fps
                # if i == 1 | i == fps - 1:
                frame = self.processor.process(frame)
                p.stdin.write(frame.tostring())
                # p.stdin.flush()
            else:
                print("队列为空")
                time.sleep(0.01)
                p.stdin.flush()

    def run(self):
        # 定义一个子进程,执行拉流
        push_frame_thread = multiprocessing.Process(target=self.pull_stream, args=())
        push_frame_thread.daemon = True  # 把子进程设置为daemon方式
        push_frame_thread.start()  # 运行子进程

        # push_frame_thread2 = multiprocessing.Process(target=self.push_stream(), args=())
        # push_frame_thread2.daemon = True  # 把子进程设置为daemon方式
        # push_frame_thread2.start()  # 运行子进程
        #
        # push_frame_thread3 = multiprocessing.Process(target=self.push_stream(), args=())
        # push_frame_thread3.daemon = True  # 把子进程设置为daemon方式
        # push_frame_thread3.start()  # 运行子进程

        # push_frame_thread4 = multiprocessing.Process(target=self.push_stream(), args=())
        # push_frame_thread4.daemon = True  # 把子进程设置为daemon方式
        # push_frame_thread4.start()  # 运行子进程

        # 推流
        self.push_stream()

# if __name__ == '__main__':
#     # rtsp://admin:baiwei460@192.168.1.64:554/1
#     # rtmp://rtmp01open.ys7.com:1935/v3/openlive/F02718370_1_1?expire=1620198018&id=309705808514265088&t=f2c7feafaefca42346647b1c862e033d2db044bd670be8b8c598f4b32f590fba&ev=100
#     detection = DetectionProcessor()
#     push = PushStream(
#         pull_rtmp_url="rtmp://rtmp01open.ys7.com:1935/v3/openlive/F02718370_1_1?expire=1620198018&id=309705808514265088&t=f2c7feafaefca42346647b1c862e033d2db044bd670be8b8c598f4b32f590fba&ev=100",
#         push_rtmp_url="rtmp://127.0.0.1:1935/stream/123", processor=detection)
#     push.run()
