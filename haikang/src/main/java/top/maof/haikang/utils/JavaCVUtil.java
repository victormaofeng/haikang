package top.maof.haikang.utils;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;
import org.springframework.stereotype.Component;

/**
 * java cv 推流 收流操作
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 * @date 2021/5/24 15:36
 */
@Slf4j
@Component
public class JavaCVUtil {
    // 本地流服务器
    public static final String NATIVE_STREAM_SERVICE = "http://127.0.0.1:1935/stream/";

    // 本地默认摄像头
    public static final int DEFAULT_NATIVE_DEVICE = 0;

    // 默认时长(毫秒)
    public static final int DEFAULT_TIME = 5 * 60 * 1000;

    /**
     * 直播或录制
     *
     * @param: [pullUrl 流源地址, deviceNumber 本地摄像头号,pushUrl 目标文件或目标流服务器地址,long 时长(毫秒)]
     * @author 毛逢
     */
    public void live(String pullUrl, int deviceNumber, String pushUrl, long time) {
        FrameGrabber frameGrabber = null;
        FrameRecorder frameRecorder = null;
        try {
            if (deviceNumber > -1) {
                // 调用本地摄像头取流
                frameGrabber = FrameGrabber.createDefault(deviceNumber);

            } else if (pullUrl != null && !"".equals(pullUrl)) {
                // 调用海康摄像头取流
                // 这种方式会默认实例化VideoInputFrameGrabber,然后报错
                // frameGrabber = FrameGrabber.createDefault(pullUrl);

                // FFmpegFrameGrabber才能获取RTMP 和 RTSP流
                frameGrabber = FFmpegFrameGrabber.createDefault(pullUrl);

            } else {
                return;
            }

            // log.info(frameGrabber.getClass().getName());

            frameGrabber.start();
            // 获取帧率
            double frameRate = frameGrabber.getFrameRate() > 20 ? frameGrabber.getFrameRate() : 20;
            // 获取视频图片宽高
            int imageWidth = frameGrabber.getImageWidth();
            int imageHeight = frameGrabber.getImageHeight();
            frameRecorder = FrameRecorder.createDefault(pushUrl, imageWidth, imageHeight);
            // 设置帧率
            frameRecorder.setFrameRate(frameRate);
            // avcodec.AV_CODEC_ID_H264，编码
            frameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            //封装格式，如果是推送到rtmp就必须是flv封装格式
            frameRecorder.setFormat("flv");
            // 开始
            frameRecorder.start();
            //转换器
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            boolean bool = true;
            long start = System.currentTimeMillis();
            while (bool) {
                Frame grabFrame = frameGrabber.grab();
                if (grabFrame == null) bool = false;
//            opencv_core.IplImage iplImage = converter.convert(grabFrame);
//            frameRecorder.record(converter.convert(iplImage));
                frameRecorder.record(grabFrame);
                if (System.currentTimeMillis() - start > time) {
                    log.info("停止");
                    break;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            if (frameGrabber != null) {
                try {
                    frameRecorder.close();
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }

            if (frameGrabber != null) {
                try {
                    frameGrabber.close();
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }

        }
    }

    /**
     * 本机摄像头直播或录制视频
     *
     * @author 毛逢
     */
    public void live(int deviceNumber, String pushUrl, long time) {
        this.live(null, deviceNumber, pushUrl, time);
    }

    /**
     * 远程摄像头直播或录制视频
     *
     * @author 毛逢
     * @date 2021/5/24 15:36
     */
    public void live(String pullUrl, String pushUrl, long time) {
        this.live(pullUrl, -1, pushUrl, time);
    }

    public void live(int deviceNumber, String pushUrl) {
        this.live(null, deviceNumber, pushUrl, DEFAULT_TIME);
    }

    public void live(String pullUrl, String pushUrl) {
        this.live(pullUrl, -1, pushUrl, DEFAULT_TIME);
    }


    public static void main(String[] args) {
        // rtsp://admin:baiwei460@192.168.1.64:554/
        JavaCVUtil javaCVUtil = new JavaCVUtil();
        javaCVUtil.live("C:\\Users\\Administrator\\Desktop\\2\\2.flv",
                "rtmp://127.0.0.1:1935/live/123", 30 * 1000);
    }
}
