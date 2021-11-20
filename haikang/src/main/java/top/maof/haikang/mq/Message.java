package top.maof.haikang.mq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import top.maof.haikang.model.Algorithm;

import java.util.Date;

@Data
public class Message {

    //已检测的ID
    private int id;

    //一大次检测的ID
    private int eid;

    // 已检测的文件路径
    private String path;

    // 文件类型,avi,mp4,png,jpg等
    private String type;

    //完成检测的插入时间
    private Date insertTime;

    //上传的时间
    private Date insertTime1;

    // 原文件路径
    private String path1;

    // 此次检测的标题
    private String title;

    // 此次检测的内容
    private String content;

    // 重识别时上传的目标人物图片地址,目标检测时该列为空
    private String path2;

    // true 监测； false 重识别
    private boolean style;
}
