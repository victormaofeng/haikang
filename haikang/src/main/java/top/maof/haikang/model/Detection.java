package top.maof.haikang.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 目标检测与重识别类
 * 纪录用户要进行的一次目标检测或重识别的操作
 *
 * @author 毛逢
 * @date 2021/9/17 16:27
 */
@Data
@ToString
public class Detection {
    private int id;

    // 此次检测的标题
    private String title;

    // 此次检测的内容
    private String content;


    // 重识别时上传的目标人物图片地址,目标检测时该列为空
    private String path;

    // 重识别时上传的目标人物图片类型,目标检测时该列为空
    private String type;

    // 用户id
    private int userId;

    private Date insertTime;

    private Date updateTime;

    // 是否为检测纪录,true 为检测,false 为重识别
    private boolean isDetect;

}
