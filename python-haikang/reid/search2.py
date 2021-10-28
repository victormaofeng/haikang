import argparse
import time

import torchvision.transforms as T

from models import *
from utils.datasets import *
from utils.utils import *

from reid.data import make_data_loader
from reid.data import make_data_loader2
from reid.data.transforms import build_transforms
from reid.modeling import build_model
from reid.config import cfg as reidCfg


def detect(cfg,
           data,
           weights,
           source_frame_list,  # 目标人物帧
           dest_frame,  # 原始图片帧
           img_size=416,
           conf_thres=0.5,
           nms_thres=0.5,
           dist_thres=1.0):
    # Initialize
    device = torch_utils.select_device(force_cpu=True)
    # device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    torch.backends.cudnn.benchmark = False  # set False for reproducible results

    ############# 行人重识别模型初始化 #############

    reidModel = build_model(reidCfg, num_classes=10126)

    reidModel.load_param(reidCfg.TEST.WEIGHT)

    reidModel.to(device).eval()

    query_feats = []

    query_loader, num_query = make_data_loader2(reidCfg, source_frame_list)

    for i, batch in enumerate(query_loader):
        with torch.no_grad():
            img = batch
            img = img.to(device)
            feat = reidModel(img)  # 一共2张待查询图片，每张图片特征向量2048 torch.Size([2, 2048])
            query_feats.append(feat)

    query_feats = torch.cat(query_feats, dim=0)  # torch.Size([2, 2048])
    print("The query feature is normalized")
    query_feats = torch.nn.functional.normalize(query_feats, dim=1, p=2)  # 计算出查询图片的特征向量

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
    opt.half = opt.half and device.type != 'cpu'  # half precision only supported on CUDA
    if opt.half:
        model.half()

    # Set Dataloader
    vid_path, vid_writer = None, None

    # dataloader = LoadImages(images, img_size=img_size, half=opt.half)

    # Get classes and colors
    # parse_data_cfg(data)['names']:得到类别名称文件路径 names=data/coco.names
    classes = load_classes(parse_data_cfg(data)['names'])  # 得到类别名列表: ['person', 'bicycle'...]
    colors = [[random.randint(0, 255) for _ in range(3)] for _ in range(len(classes))]  # 对于每种类别随机使用一种颜色画框

    # ================将原始图片resize等处理后放入网络================

    img0 = dest_frame  # BGR HWC: (1080, 810, 3)

    # Padded resize
    img, *_ = letterbox(img0, new_shape=img_size)  # img经过padding后的最小输入矩形图: (416, 320, 3)

    # cv2.imshow('Padded Image', img)
    # cv2.waitKey()

    # Normalize RGB
    img = img[:, :, ::-1].transpose(2, 0, 1)  # BGR2RGB  HWC2CHW: (3, 416, 320)
    # ascontiguousarray函数将一个内存不连续存储的数组转换为内存连续存储的数组，使得运行速度更快。
    img = np.ascontiguousarray(img, dtype=np.float16 if opt.half else np.float32)  # uint8 to fp16/fp32
    img /= 255.0  # 0 - 255 to 0.0 - 1.0

    # cv2.imwrite(path + '.letterbox.jpg', 255 * img.transpose((1, 2, 0))[:, :, ::-1])  # save letterbox image
    # return path, img, img0, self.cap

    # ================================

    # Run inference

    # for i, (path, img, img0, vid_cap) in enumerate(dataloader):
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
                    crop_img = img0[ymin:ymax, xmin:xmax]  # HWC (602, 233, 3)
                    crop_img = Image.fromarray(cv2.cvtColor(crop_img, cv2.COLOR_BGR2RGB))  # PIL: (233, 602)
                    crop_img = build_transforms(reidCfg)(crop_img).unsqueeze(0)  # torch.Size([1, 3, 256, 128])
                    gallery_img.append(crop_img)

        if gallery_img:
            gallery_img = torch.cat(gallery_img, dim=0)  # torch.Size([7, 3, 256, 128])
            gallery_img = gallery_img.to(device)
            gallery_feats = reidModel(gallery_img)  # torch.Size([7, 2048])
            print("The gallery feature is normalized")
            gallery_feats = torch.nn.functional.normalize(gallery_feats, dim=1, p=2)  # 计算出查询图片的特征向量

            # m: 2
            # n: 7
            m, n = query_feats.shape[0], gallery_feats.shape[0]
            distmat = torch.pow(query_feats, 2).sum(dim=1, keepdim=True).expand(m, n) + \
                      torch.pow(gallery_feats, 2).sum(dim=1, keepdim=True).expand(n, m).t()
            # out=(beta∗M)+(alpha∗mat1@mat2)
            # qf^2 + gf^2 - 2 * qf@gf.t()
            # distmat - 2 * qf@gf.t()
            # distmat: qf^2 + gf^2
            # qf: torch.Size([2, 2048])
            # gf: torch.Size([7, 2048])
            distmat.addmm_(1, -2, query_feats, gallery_feats.t())
            # distmat = (qf - gf)^2
            # distmat = np.array([[1.79536, 2.00926, 0.52790, 1.98851, 2.15138, 1.75929, 1.99410],
            #                     [1.78843, 1.96036, 0.53674, 1.98929, 1.99490, 1.84878, 1.98575]])
            distmat = distmat.cpu().numpy()  # <class 'tuple'>: (3, 12)
            distmat = distmat.sum(axis=0) / len(query_feats)  # 平均一下query中同一行人的多个结果
            index = distmat.argmin()
            if distmat[index] < dist_thres:
                print('距离：%s' % distmat[index])
                plot_one_box(gallery_loc[index], img0, label='aim', color=colors[int(cls)])
                cv2.imshow('person search', img0)
                cv2.waitKey()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--cfg', type=str, default='cfg/yolov3.cfg', help="模型配置文件路径")
    parser.add_argument('--data', type=str, default='data/coco.data', help="数据集配置文件所在路径")
    parser.add_argument('--weights', type=str, default='weights/yolov3.weights', help='模型权重文件路径')
    parser.add_argument('--images', type=str, default='data/samples', help='需要进行检测的图片文件夹')
    parser.add_argument('-q', '--query', default=r'query', help='查询图片的读取路径.')
    parser.add_argument('--img-size', type=int, default=416, help='输入分辨率大小')
    parser.add_argument('--conf-thres', type=float, default=0.1, help='物体置信度阈值')
    parser.add_argument('--nms-thres', type=float, default=0.4, help='NMS阈值')
    parser.add_argument('--dist_thres', type=float, default=1.0, help='行人图片距离阈值，小于这个距离，就认为是该行人')
    parser.add_argument('--fourcc', type=str, default='mp4v', help='fourcc output video codec (verify ffmpeg support)')
    parser.add_argument('--output', type=str, default='output', help='检测后的图片或视频保存的路径')
    parser.add_argument('--half', default=False, help='是否采用半精度FP16进行推理')
    opt = parser.parse_args()
    # print(opt)

    with torch.no_grad():
        detect(opt.cfg,
               opt.data,
               opt.weights,
               source_frame_list=[Image.open("query/0001_c1s1_001051_00.jpg").convert('RGB')],
               dest_frame=cv2.imread("data/samples/c1s1_002301.jpg"),
               img_size=opt.img_size,
               conf_thres=opt.conf_thres,
               nms_thres=opt.nms_thres,
               dist_thres=opt.dist_thres)
