# encoding: utf-8
"""
@author:  liaoxingyu
@contact: sherlockliao01@gmail.com
"""

from torch.utils.data import DataLoader
import torch

from .collate_batch import val_collate_fn
from .datasets import ImageDataset
from .datasets import ImageDataset2
from .transforms import build_transforms
from .datasets import Market1501


def make_data_loader(cfg):
    # 验证集的预处理
    val_transforms = build_transforms(cfg)
    num_workers = cfg.DATALOADER.NUM_WORKERS  # 加载图像进程数 8
    dataset = Market1501(root=cfg.DATASETS.ROOT_DIR)

    val_set = ImageDataset(dataset.query, val_transforms)

    val_loader = DataLoader(
        val_set, batch_size=cfg.TEST.IMS_PER_BATCH, shuffle=False, num_workers=num_workers,
        collate_fn=val_collate_fn
    )
    return val_loader, len(dataset.query)


def make_data_loader2(cfg, query_list):
    """
    maofeng

    重新写的data_loader,上面的data_loader代码默认加载的时market1501数据集
    """
    # 验证集的预处理
    val_transforms = build_transforms(cfg)

    num_workers = cfg.DATALOADER.NUM_WORKERS  # 加载图像进程数 8

    val_set = ImageDataset2(query_list, val_transforms)

    val_loader = DataLoader(
        val_set, batch_size=cfg.TEST.IMS_PER_BATCH, shuffle=False, num_workers=num_workers,
        collate_fn=val_collate_fn2
    )
    return val_loader, len(query_list)


def val_collate_fn2(batch):
    # imgs = zip(*batch)
    return torch.stack(batch, dim=0)
