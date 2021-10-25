package top.maof.haikang;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

/**
 * 调用本地摄像头窗口视频
 *
 * @author eguid
 * @date 2016年6月13日
 * @since javacv1.2
 */
@Slf4j
public class JavavcCameraTest {
    public static void main(String[] args) throws Exception, InterruptedException, FrameRecorder.Exception {
        File file=new File("C:\\Users\\Administrator\\Desktop","/2/1.flv");
        String os = System.getProperty("os.name");
        log.info(file.getPath());
        log.info(os);
    }

    public static void nativeCameraShow() throws Exception, InterruptedException {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("rtsp://admin:baiwei460@192.168.1.64:554/1");
        //新建opencv抓取器，一般的电脑和移动端设备中摄像头默认序号是0，不排除其他情况
        grabber.start();//开始获取摄像头数据

        CanvasFrame canvas = new CanvasFrame("摄像头预览");//新建一个预览窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //窗口是否关闭
        while (canvas.isDisplayable()) {
            /*获取摄像头图像并在窗口中显示,这里Frame frame=grabber.grab()得到是解码后的视频图像*/
            canvas.showImage(grabber.grab());
        }
        grabber.close();//停止抓取
    }

    /**
     * 按帧录制本机摄像头视频（边预览边录制，停止预览即停止录制）
     *
     * @param outputFile -录制的文件路径，也可以是rtsp或者rtmp等流媒体服务器发布地址
     * @throws Exception
     * @throws InterruptedException
     * @throws org.bytedeco.javacv.FrameRecorder.Exception
     * @author eguid
     */
    public static void recordCamera(String outputFile) throws Exception, FrameRecorder.Exception, InterruptedException {
        //另一种方式获取摄像头，opencv抓取器方式获取摄像头请参考第一章，FrameGrabber会自己去找可以打开的摄像头的抓取器。
        FrameGrabber grabber = FrameGrabber.createDefault(0);//本机摄像头默认0
        grabber.start();//开启抓取器

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();//转换器
        opencv_core.IplImage grabbedImage = converter.convert(grabber.grab());//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
        // 获取图像宽高
        int width = grabbedImage.width();
        int height = grabbedImage.height();
        // 获取视频帧率
        double frameRate = grabber.getFrameRate() > 10 ? grabber.getFrameRate() : 10.0;

        FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height);
        log.info(recorder.getClass().getName());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
        recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFrameRate(frameRate);

        recorder.start();//开启录制器
        long startTime = 0;
        long videoTS = 0;
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        Frame rotatedFrame = null;//不知道为什么这里不做转换就不能推到rtmp

        while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {

            rotatedFrame = converter.convert(grabbedImage);

            frame.showImage(rotatedFrame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = 1000 * (System.currentTimeMillis() - startTime);

            recorder.setTimestamp(videoTS);

            recorder.record(rotatedFrame);

            Thread.sleep(40);
        }
        frame.dispose();//关闭窗口
        recorder.close();//关闭推流录制器，close包含release和stop操作
        grabber.close();//关闭抓取器

    }
}