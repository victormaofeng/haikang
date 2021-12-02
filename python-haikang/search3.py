import argparse
import time

import numpy
import torchvision.transforms as T

from models import *
from utils.datasets import *
from utils.utils1 import *

from algo import DetectionProcessor


def detect(cfg, data, weights, dest_frame, img_size=416, conf_thres=0.5, nms_thres=0.5, half=False):
    # Initialize 是否使用cpu
    device = torch_utils.select_device(force_cpu=True)
    # device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    torch.backends.cudnn.benchmark = False  # set False for reproducible results

    ############# 行人检测模型初始化 #############

    model = Darknet(cfg, img_size)

    # Load weights
    if weights.endswith('.pt'):  # pytorch format
        model.load_state_dict(torch.load(weights, map_location=device)['model'])
    else:  # darknet format
        _ = load_darknet_weights(model, weights)

    # Eval mode
    model.to(device).eval()
    # Half precision
    half = half and device.type != 'cpu'  # half precision only supported on CUDA
    if half:
        model.half()

    # parse_data_cfg(data)['names']:得到类别名称文件路径 names=data/coco.names
    # 获取目标检测的目标类别 其中包括 person car bus等等
    classes = load_classes(parse_data_cfg(data)['names'])  # 得到类别名列表: ['person', 'bicycle'...]
    # 获取目标类别对应的框的随机颜色
    colors = [[random.randint(0, 255) for _ in range(3)] for _ in range(len(classes))]  # 对于每种类别随机使用一种颜色画框

    # ================将原始图片resize等处理后放入网络================

    img0 = dest_frame  # BGR HWC: (1080, 810, 3)

    # Padded resize
    img, *_ = letterbox(img0, new_shape=img_size)  # img经过padding后的最小输入矩形图: (416, 320, 3)

    # dest_frame为cv2获取的numpy,默认为BGR格式,将其转成RGB
    img = img[:, :, ::-1].transpose(2, 0, 1)  # BGR2RGB  HWC2CHW: (3, 416, 320)
    # ascontiguousarray函数将一个内存不连续存储的数组转换为内存连续存储的数组，使得运行速度更快。
    img = np.ascontiguousarray(img, dtype=np.float16 if half else np.float32)  # uint8 to fp16/fp32
    img /= 255.0  # 0 - 255 to 0.0 - 1.0

    # Get detections shape: (3, 416, 320)
    img = torch.from_numpy(img).unsqueeze(0).to(device)  # torch.Size([1, 3, 416, 320])
    pred, _ = model(img)  # 经过处理的网络预测，和原始的
    det = non_max_suppression(pred.float(), conf_thres, nms_thres)[0]  # torch.Size([5, 7])

    if det is not None and len(det) > 0:
        # Rescale boxes from 416 to true image size 映射到原图
        det[:, :4] = scale_coords(img.shape[2:], det[:, :4], img0.shape).round()

        # Print results to screen image 1/3 data\samples\000493.jpg: 288x416 5 persons, Done. (0.869s)
        print('%gx%g ' % img.shape[2:], end='')  # print image size '288x416'
        for c in det[:, -1].unique():  # 对图片的所有类进行遍历循环
            n = (det[:, -1] == c).sum()  # 得到了当前类别的个数，也可以用来统计数目
            if classes[int(c)] == 'person':
                print('%g %ss' % (n, classes[int(c)]), end=', ')  # 打印个数和类别'5 persons'

        # Draw bounding boxes and labels of detections
        # (x1y1x2y2, obj_conf, class_conf, class_pred)
        count = 0
        gallery_img = []
        gallery_loc = []
        for *xyxy, conf, cls_conf, cls in det:  # 对于最后的预测框进行遍历

            # Add bbox to the image，画框
            label = '%s %.2f' % (classes[int(cls)], conf)  # 'person 1.00'
            if classes[int(cls)] == 'person':
                # plot_one_bo x(xyxy, img0, label=label, color=colors[int(cls)])
                xmin = int(xyxy[0])
                ymin = int(xyxy[1])
                xmax = int(xyxy[2])
                ymax = int(xyxy[3])
                w = xmax - xmin  # 233
                h = ymax - ymin  # 602
                # 如果检测到的行人太小了，感觉意义也不大
                # 这里需要根据实际情况稍微设置下
                if w * h > 500:
                    gallery_loc.append((xmin, ymin, xmax, ymax))

        for box in gallery_loc:
            plot_one_box(box, img0, label='p', color=colors[int(cls)])

    return img0


def process(dest_frame):
    with torch.no_grad():
        result = detect(cfg='cfg/yolov3.cfg',
                        data='data/coco.data',
                        weights='weights/yolov3.weights',
                        dest_frame=dest_frame,
                        img_size=416,
                        conf_thres=0.1,
                        nms_thres=0.4)
        return result


class SimpleDarknetDetectionProcessor(DetectionProcessor):

    def __init__(self, cfg='cfg/yolov3.cfg',
                 data='data/coco.data',
                 weights='weights/yolov3.weights',
                 img_size=416,
                 half=False,
                 conf_thres=0.1,
                 nms_thres=0.4):

        self.img_size = img_size
        self.cfg = cfg
        self.conf_thres = conf_thres
        self.nms_thres = nms_thres
        with torch.no_grad():

            self.device = torch_utils.select_device(force_cpu=True)
            # device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
            torch.backends.cudnn.benchmark = False  # set False for reproducible results

            ############# 行人检测模型初始化 #############

            self.model = Darknet(cfg, img_size)

            # Load weights
            if weights.endswith('.pt'):  # pytorch format
                self.model.load_state_dict(torch.load(weights, map_location=self.device)['model'])
            else:  # darknet format
                _ = load_darknet_weights(self.model, weights)

            # Eval mode
            self.model.to(self.device).eval()
            # Half precision
            self.half = half and self.device.type != 'cpu'  # half precision only supported on CUDA
            if half:
                self.model.half()
            # parse_data_cfg(data)['names']:得到类别名称文件路径 names=data/coco.names
            # 获取目标检测的目标类别 其中包括 person car bus等等
            self.classes = load_classes(parse_data_cfg(data)['names'])  # 得到类别名列表: ['person', 'bicycle'...]
            # 获取目标类别对应的框的随机颜色
            self.colors = [[random.randint(0, 255) for _ in range(3)] for _ in
                           range(len(self.classes))]  # 对于每种类别随机使用一种颜色画框

    def process(self, frame: numpy.ndarray):
        with torch.no_grad():
            img0 = frame  # BGR HWC: (1080, 810, 3)
            # Padded resize
            img, *_ = letterbox(img0, new_shape=self.img_size)  # img经过padding后的最小输入矩形图: (416, 320, 3)

            # dest_frame为cv2获取的numpy,默认为BGR格式,将其转成RGB
            img = img[:, :, ::-1].transpose(2, 0, 1)  # BGR2RGB  HWC2CHW: (3, 416, 320)
            # ascontiguousarray函数将一个内存不连续存储的数组转换为内存连续存储的数组，使得运行速度更快。
            img = np.ascontiguousarray(img, dtype=np.float16 if self.half else np.float32)  # uint8 to fp16/fp32
            img /= 255.0  # 0 - 255 to 0.0 - 1.0

            # Get detections shape: (3, 416, 320)
            img = torch.from_numpy(img).unsqueeze(0).to(self.device)  # torch.Size([1, 3, 416, 320])
            pred, _ = self.model(img)  # 经过处理的网络预测，和原始的
            det = non_max_suppression(pred.float(), self.conf_thres, self.nms_thres)[0]  # torch.Size([5, 7])

            if det is not None and len(det) > 0:
                # Rescale boxes from 416 to true image size 映射到原图
                det[:, :4] = scale_coords(img.shape[2:], det[:, :4], img0.shape).round()

                # Print results to screen image 1/3 data\samples\000493.jpg: 288x416 5 persons, Done. (0.869s)
                print('%gx%g ' % img.shape[2:], end='')  # print image size '288x416'
                for c in det[:, -1].unique():  # 对图片的所有类进行遍历循环
                    n = (det[:, -1] == c).sum()  # 得到了当前类别的个数，也可以用来统计数目
                    if self.classes[int(c)] == 'person':
                        print('%g %ss' % (n, self.classes[int(c)]), end=', ')  # 打印个数和类别'5 persons'

                # Draw bounding boxes and labels of detections
                # (x1y1x2y2, obj_conf, class_conf, class_pred)
                count = 0
                gallery_img = []
                gallery_loc = []
                for *xyxy, conf, cls_conf, cls in det:  # 对于最后的预测框进行遍历

                    # Add bbox to the image，画框
                    label = '%s %.2f' % (self.classes[int(cls)], conf)  # 'person 1.00'
                    if self.classes[int(cls)] == 'person':
                        # plot_one_bo x(xyxy, img0, label=label, color=colors[int(cls)])
                        xmin = int(xyxy[0])
                        ymin = int(xyxy[1])
                        xmax = int(xyxy[2])
                        ymax = int(xyxy[3])
                        w = xmax - xmin  # 233
                        h = ymax - ymin  # 602
                        # 如果检测到的行人太小了，感觉意义也不大
                        # 这里需要根据实际情况稍微设置下
                        if w * h > 500:
                            gallery_loc.append((xmin, ymin, xmax, ymax))

                for box in gallery_loc:
                    plot_one_box(box, img0, label='p', color=self.colors[int(cls)])

            return img0


class DarknetDetectionProcessor(DetectionProcessor):

    def __init__(self, cfg='cfg/yolov3.cfg',
                 data='data/coco.data',
                 weights='weights/yolov3.weights',
                 img_size=416,
                 half=False,
                 conf_thres=0.1,
                 nms_thres=0.6):

        self.img_size = img_size
        self.cfg = cfg
        self.conf_thres = conf_thres
        self.nms_thres = nms_thres
        with torch.no_grad():

            self.device = torch_utils.select_device(force_cpu=True)
            # device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
            torch.backends.cudnn.benchmark = False  # set False for reproducible results

            ############# 行人检测模型初始化 #############

            self.model = Darknet(cfg, img_size)

            # Load weights
            if weights.endswith('.pt'):  # pytorch format
                self.model.load_state_dict(torch.load(weights, map_location=self.device)['model'])
            else:  # darknet format
                _ = load_darknet_weights(self.model, weights)

            # Eval mode
            self.model.to(self.device).eval()
            # Half precision
            self.half = half and self.device.type != 'cpu'  # half precision only supported on CUDA
            if half:
                self.model.half()
            # parse_data_cfg(data)['names']:得到类别名称文件路径 names=data/coco.names
            # 获取目标检测的目标类别 其中包括 person car bus等等
            self.classes = load_classes(parse_data_cfg(data)['names'])  # 得到类别名列表: ['person', 'bicycle'...]
            # 获取目标类别对应的框的随机颜色
            self.colors = [[random.randint(0, 255) for _ in range(3)] for _ in
                           range(len(self.classes))]  # 对于每种类别随机使用一种颜色画框

    def process(self, frame: numpy.ndarray):
        with torch.no_grad():
            img0 = frame  # BGR HWC: (1080, 810, 3)
            # Padded resize
            img, *_ = letterbox(img0, new_shape=self.img_size)  # img经过padding后的最小输入矩形图: (416, 320, 3)

            # dest_frame为cv2获取的numpy,默认为BGR格式,将其转成RGB
            img = img[:, :, ::-1].transpose(2, 0, 1)  # BGR2RGB  HWC2CHW: (3, 416, 320)
            # ascontiguousarray函数将一个内存不连续存储的数组转换为内存连续存储的数组，使得运行速度更快。
            img = np.ascontiguousarray(img, dtype=np.float16 if self.half else np.float32)  # uint8 to fp16/fp32
            img /= 255.0  # 0 - 255 to 0.0 - 1.0

            # Get detections shape: (3, 416, 320)
            img = torch.from_numpy(img).unsqueeze(0).to(self.device)  # torch.Size([1, 3, 416, 320])
            pred, _ = self.model(img)  # 经过处理的网络预测，和原始的
            det = non_max_suppression(pred.float(), self.conf_thres, self.nms_thres)[0]  # torch.Size([5, 7])

            if det is not None and len(det) > 0:
                # Rescale boxes from 416 to true image size 映射到原图
                det[:, :4] = scale_coords(img.shape[2:], det[:, :4], img0.shape).round()

                # Print results to screen image 1/3 data\samples\000493.jpg: 288x416 5 persons, Done. (0.869s)
                print('%gx%g ' % img.shape[2:], end='')  # print image size '288x416'
                for c in det[:, -1].unique():  # 对图片的所有类进行遍历循环
                    n = (det[:, -1] == c).sum()  # 得到了当前类别的个数，也可以用来统计数目
                    if self.classes[int(c)] == 'person':
                        print('%g %ss' % (n, self.classes[int(c)]), end=', ')  # 打印个数和类别'5 persons'

                # Draw bounding boxes and labels of detections
                # (x1y1x2y2, obj_conf, class_conf, class_pred)
                count = 0
                gallery_img = []
                gallery_loc = []
                for *xyxy, conf, cls_conf, cls in det:  # 对于最后的预测框进行遍历

                    # Add bbox to the image，画框
                    label = '%s %.2f' % (self.classes[int(cls)], conf)  # 'person 1.00'
                    if self.classes[int(cls)] == 'person':
                        # plot_one_bo x(xyxy, img0, label=label, color=colors[int(cls)])
                        xmin = int(xyxy[0])
                        ymin = int(xyxy[1])
                        xmax = int(xyxy[2])
                        ymax = int(xyxy[3])
                        w = xmax - xmin  # 233
                        h = ymax - ymin  # 602
                        # 如果检测到的行人太小了，感觉意义也不大
                        # 这里需要根据实际情况稍微设置下
                        if w * h > 500:
                            gallery_loc.append((xmin, ymin, xmax, ymax))

                for box in gallery_loc:
                    plot_one_box(box, img0, label='p', color=self.colors[int(cls)])

            return img0


if __name__ == '__main__':
    # parser = argparse.ArgumentParser()
    # parser.add_argument('--cfg', type=str, default='cfg/yolov3.cfg', help="模型配置文件路径")
    # parser.add_argument('--data', type=str, default='data/coco.data', help="数据集配置文件所在路径")
    # parser.add_argument('--weights', type=str, default='weights/yolov3.weights', help='模型权重文件路径')
    # parser.add_argument('--images', type=str, default='data/samples', help='需要进行检测的图片文件夹')
    # parser.add_argument('-q', '--query', default=r'query', help='查询图片的读取路径.')
    # parser.add_argument('--img_size', type=int, default=416, help='输入分辨率大小')
    # parser.add_argument('--conf_thres', type=float, default=0.1, help='物体置信度阈值')
    # parser.add_argument('--nms_thres', type=float, default=0.4, help='NMS阈值')
    # parser.add_argument('--dist_thres', type=float, default=1.0, help='行人图片距离阈值，小于这个距离，就认为是该行人')
    # parser.add_argument('--fourcc', type=str, default='mp4v', help='fourcc output video codec (verify ffmpeg support)')
    # parser.add_argument('--output', type=str, default='output', help='检测后的图片或视频保存的路径')
    # parser.add_argument('--half', default=False, help='是否采用半精度FP16进行推理')
    # opt = parser.parse_args()

    # with torch.no_grad():
    # frame = process(
    #     dest_frame=cv2.imread("data/samples/c1s1_002301.jpg"))
    # cv2.imshow('person search', frame)
    # cv2.waitKey()
    model = DarknetDetectionProcessor()
    img = model.process(cv2.imread("C:\\Users\\Administrator\\Desktop\\2.png"))
    # cv2.imshow('person search', img)
    # cv2.waitKey()
    cv2.imwrite("C:\\Users\\Administrator\\Desktop\\22.png", img)
