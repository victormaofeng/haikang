package top.maof.haikang.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 目标检测中待检测的文件
 * 包含图片和视频
 *
 * @author 毛逢
 * @date 2021/9/17 16:29
 */
@Data
@ToString
public class DetectFile {
    private int id;

    // 文件路径
    private String path;

    // 文件类型,avi,mp4,png,jpg等
    private String type;

    // 属于哪次目标检测
    private int detectionId;

    private Date insertTime;

    private Date updateTime;
}
