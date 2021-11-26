package top.maof.haikang.vo;


import lombok.Data;

import java.util.Date;

@Data
public class DetectDetailVo {

    //一大次的ID
    private int id;

    // 此次检测的标题
    private String title;

    //此次检测的内容
    private String content;

    //user_id
    private int userId;

    // 是否为检测纪录,true 为检测,false 为重识别
    private boolean isDetect;

    // 重识别时上传的目标人物图片地址,目标检测时该列为空
    private String path2;

    //上传时插入数据库中的ID
    private int eid;

    // 文件类型,avi,mp4,png,jpg等
    private String type;

    // 原文件路径
    private String path1;

    //上传的时间
    private Date insertTime;

    // 已检测的文件路径
    private String path;

    //完成检测的插入时间
    private Date insertTime1;

}
