package top.maof.haikang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.maof.haikang.type.VideoType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Video {

    private Integer id;
    // 标题
    private String title;
    // 简介
    private String synopsis;


    // 视频时长
    private long duration;
    // 创建时间
    private Date date;
    // 原文件相对路径
    private String path;
    // 原文件格式
    private String type;

    // 转码后相对路径
    private String convertPath;
    // 转码后的文件格式
    private String convertType;
    // 封面地址
    private String coverImage;

}
