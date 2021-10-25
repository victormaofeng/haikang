package top.maof.haikang.model;

import lombok.Data;

import java.util.Date;

/**
 * 目表检测已经检测完成的文件
 * 包含图片和视频
 *
 * @author 毛逢
 * @date 2021/9/17 16:32
 */
@Data
public class DetectedFile {

    private int id;

    // 文件路径
    private String path;

    // 文件类型,avi,mp4,png,jpg等
    private String type;

    // 属于哪个待检测的文件
    private int detectFileId;

    // 检测时使用的算法
    private int algoId;

    private Date insertTime;

    private Date updateTime;
}
