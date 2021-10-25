package top.maof.haikang.type;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 视频类型
 *
 * @author 毛逢
 * @date 2021/9/16 10:52
 */
public enum VideoType {
    FLV("flv"), MP4("mp4"), HLS("m3u8"), AVI("avi");

    private String type;

    private VideoType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
