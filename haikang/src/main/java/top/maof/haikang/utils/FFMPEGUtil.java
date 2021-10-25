package top.maof.haikang.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 转码器工具类,使用前请安装ffmpeg软件,并配置好环境变量
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 * @date 2021/5/19 15:48
 */
@Component
@Slf4j
public class FFMPEGUtil {


    /**
     * ffmpeg程序将asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等转成m3u8
     *
     * @param: [filePath 视频地址, convertFilePath 转码视频保存地址]
     * @return: boolean 是否转码成功
     * @author 毛逢
     * @eamil 3286408344@qq.com
     * @date: 2021/5/19 15:46
     */
    public boolean convertToM3u8(String filePath, String convertFilePath) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        //这里就写入执行语句就可以了
        List<String> command = new ArrayList();
        command.add("ffmpeg.exe");
        command.add("-i");
        command.add(filePath);
        command.add("-c:v");
        command.add("libx264");
        command.add("-hls_time");
        command.add("20");
        command.add("-hls_list_size");
        command.add("0");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
        command.add("-f");
        command.add("hls");
        command.add(convertFilePath);
        processBuilder.command(command);
        try {
            //启动进程
            Process p = processBuilder.start();
            int i = doWaitFor(p);
            log.info("----->" + i);
            p.destroy();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 监听ffmpeg运行过程
     *
     * @param p
     * @return
     */
    private int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1; // returned to caller when p is finished
        try {
            log.info("coming");
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false; // Set to true when p is finished
            while (!finished) {
                try {
                    byte[] bytes = new byte[1024];
                    int length = 0;
                    while (in.available() > 0 && (length = in.read(bytes)) > 0) {
                        log.info(new String(bytes, 0, length));
                    }
                    while (err.available() > 0 && (length = err.read(bytes)) > 0) {
                        log.info(new String(bytes, 0, length));
                    }
                    exitValue = p.exitValue();
                    finished = true;
                } catch (IllegalThreadStateException e) {
                    Thread.currentThread().sleep(500);
                }
            }
        } catch (Exception e) {
            log.info("doWaitFor();: unexpected exception - "
                    + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }

            try {
                if (err != null) {
                    err.close();
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }

        }
        return exitValue;
    }

    public static void main(String[] args) {
        FFMPEGUtil ffmpegUtil = new FFMPEGUtil();
        ffmpegUtil.convertToM3u8("C:\\Users\\Administrator\\Desktop\\1\\1\\1.flv",
                "C:\\Users\\Administrator\\Desktop\\1\\1\\2.m3u8");
    }

}
