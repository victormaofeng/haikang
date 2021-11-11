package top.maof.haikang.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.maof.haikang.config.RabbitMQConfig;
import top.maof.haikang.config.WebMvcConfig;
import top.maof.haikang.model.DetectFile;
import top.maof.haikang.model.Detection;
import top.maof.haikang.mq.DetectionMessage;
import top.maof.haikang.result.Result;
import top.maof.haikang.service.DetectFileService;
import top.maof.haikang.service.DetectionService;
import top.maof.haikang.utils.JWTUtil;
import top.maof.haikang.vo.DetectDetailVo;
import top.maof.haikang.vo.PageVO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * 目标检测
 *
 * @author 毛逢
 * @date 2021/9/17 16:38
 */
@Controller
@RequestMapping("/detect")
@Slf4j
public class DetectionController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    DetectionService detectionService;

    @Autowired
    DetectFileService detectFileService;

    /**
     * int page, int pageSize, String token, int type
     * type 0 所有,1 检测,2 重识别
     */
    @ResponseBody
    @RequestMapping("/list")
    public Result list(int page, int pageSize, String token, int type) {
        int userId = JWTUtil.getUserId(token).intValue();
        PageVO<Detection> pageVo = detectionService.list(userId, type, page, pageSize);
        return Result.success(pageVo);
    }


    /**
     * int page, int pageSize, String token, int type
     * type 0 所有,1 检测,2 重识别
     */
    @ResponseBody
    @RequestMapping("/listDetails")
    public Result listDetails(int page, int pageSize, String token, int type, int id) {
        int userId = JWTUtil.getUserId(token).intValue();
        PageVO<DetectDetailVo> pageVo = detectionService.listDetail(userId, type, page, pageSize, id);
        return Result.success(pageVo);
    }



    /**
     * 用户进行一次目标检测
     */
    @PostMapping("")
    @ResponseBody
    public Result detect(MultipartFile[] files, String title, String content,
        int algorithmId, String token) throws Exception {

        // 目标检测对象
        Detection detection = new Detection();
        detection.setContent(content);
        detection.setTitle(title);
        // 检测纪录
        detection.setDetect(true);
        Integer userId = JWTUtil.getUserId(token);
        detection.setUserId(userId);
        detection.setInsertTime(new Date());
        log.info(detection.toString());
        int detectionId = detectionService.insert(detection);

        // 插入失败
        if (detectionId < 1) {
            // 服务器错误
            return Result.response_500();
        }

        log.info("文件:" + files.length);

        for (MultipartFile file : files) {

            String originalFilename = file.getOriginalFilename();

            String extensionName = getExtension(originalFilename);

            // 判断文件格式是否符合要求
            if (!isVideo(extensionName)) {
                continue;
                // return Result.response_400();
            }

            String child = getRelativePath(originalFilename, extensionName);

            File newFile = createFile(child);

            // MultipartFile 提供了自动保存功能,但是无法知晓上传进度,所以改为手动保存
            // file.transferTo(newFile);

            InputStream inputStream = file.getInputStream();

            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            try {
                // 文件保存
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b)) != -1) {
                    fileOutputStream.write(b, 0, len);
                }

                // 一个文件对应一个 DetectFile
                DetectFile detectFile = new DetectFile();
                detectFile.setDetectionId(detectionId);
                detectFile.setPath(child);
                detectFile.setInsertTime(new Date());
                detectFile.setType(extensionName);
                int detectFileId = detectFileService.insert(detectFile);

                // 待处理视频或图像插入失败,下一个
                if (detectFileId < 0)
                    continue;

                log.info(detectFile.toString());

                // 设置 java python 消息
                DetectionMessage detectionMessage = new DetectionMessage();

                // 设置算法
                detectionMessage.setAlgoId(algorithmId);

                // 设置detectFile 信息
                detectionMessage.setDetectId(detectFileId);
                detectionMessage.setDetectPath(child);
                detectionMessage.setDetectType(extensionName);

                // 设置detectedFile信息
                detectionMessage.setDetectedPath(getRelativePath(originalFilename, extensionName));
                detectionMessage.setDetectedType(extensionName);

                // 设置绝对路径
                detectionMessage.setRootPath(WebMvcConfig.uploadRootPath());

                detectionMessage.setToken(token);

                log.info(detectionMessage.toString());

                // 文件上传并保存成功,向python端发送目标检测消息
                rabbitTemplate.convertAndSend(RabbitMQConfig.DETECTION_TOPIC_EXCHANGE,
                    RabbitMQConfig.DETECTION_ROUTING_KEY,
                    JSON.toJSON(detectionMessage));

            } catch (Exception e) {
                // return Result.response_500();
            } finally {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
        }
        return Result.success();
    }

    /**
     * 用户进行一次重识别
     */
    @PostMapping("/reid")
    @ResponseBody
    public Result reid(MultipartFile[] files, MultipartFile img, String title, String content,
        int algorithmId, String token) throws Exception {

        Integer userId = JWTUtil.getUserId(token);

        Detection detection = new Detection();
        detection.setContent(content);
        detection.setTitle(title);

        // 重识别纪录
        detection.setDetect(false);

        detection.setUserId(userId);

        detection.setInsertTime(new Date());

        String imgExtension = getExtension(img.getOriginalFilename());

        if (!isImg(imgExtension))
            return Result.response_400();

        String imgRelativePath = getRelativePath(img.getOriginalFilename(), imgExtension);

        File saveImg = createFile(imgRelativePath);

        img.transferTo(saveImg);

        // 设置识别的目标人物
        detection.setPath(imgRelativePath);
        detection.setType(imgExtension);

        int detectionId = detectionService.insert(detection);

        // 插入失败
        if (detectionId < 1) {
            // 服务器错误
            return Result.response_500();
        }

        log.info("文件:" + files.length);

        for (MultipartFile file : files) {

            String originalFilename = file.getOriginalFilename();

            String extensionName = getExtension(originalFilename);

            // 判断文件格式是否符合要求
            if (!isVideo(extensionName)) {
                continue;
                // return Result.response_400();
            }

            String child = getRelativePath(originalFilename, extensionName);

            File newFile = createFile(child);

            // MultipartFile 提供了自动保存功能,但是无法知晓上传进度,所以改为手动保存
            // file.transferTo(newFile);

            InputStream inputStream = file.getInputStream();

            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            try {
                // 文件保存
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b)) != -1) {
                    fileOutputStream.write(b, 0, len);
                }

                // 一个文件对应一个 DetectFile
                DetectFile detectFile = new DetectFile();
                detectFile.setDetectionId(detectionId);
                detectFile.setPath(child);
                detectFile.setInsertTime(new Date());
                detectFile.setType(extensionName);

                int detectFileId = detectFileService.insert(detectFile);

                // 待处理视频或图像插入失败,下一个
                if (detectFileId < 0)
                    continue;

                // 设置 java python 消息
                DetectionMessage detectionMessage = new DetectionMessage();

                // 设置重识别时的目标图片
                detectionMessage.setPath(imgRelativePath);
                detectionMessage.setType(imgExtension);

                // 设置算法
                detectionMessage.setAlgoId(algorithmId);

                // 设置detectFile 信息(待处理的图片或视频地址和类型)
                detectionMessage.setDetectId(detectFileId);
                detectionMessage.setDetectPath(child);
                detectionMessage.setDetectType(extensionName);

                // 设置detectedFile信息(处理完成的图片或视频的地址和类型)
                detectionMessage.setDetectedPath(getRelativePath(originalFilename, extensionName));
                detectionMessage.setDetectedType(extensionName);

                // 设置绝对路径
                detectionMessage.setRootPath(WebMvcConfig.uploadRootPath());

                // 设置token,以便通过websocket通知前端任务完成
                detectionMessage.setToken(token);

                // 文件上传并保存成功,向python端发送目标检测消息
                rabbitTemplate.convertAndSend(RabbitMQConfig.REID_TOPIC_EXCHANGE, RabbitMQConfig.REID_ROUTING_KEY,
                    JSON.toJSON(detectionMessage));

            } catch (Exception e) {
                // return Result.response_500();
            } finally {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
        }
        return Result.success();
    }

    public File createFile(String relativePath) throws Exception {

        File newFile = new File(WebMvcConfig.uploadRootPath(), relativePath);

        if (!newFile.exists()) {
            if (!newFile.getParentFile().exists()) {
                // 创建目录
                boolean isMkdir = newFile.getParentFile().mkdirs();
                if (isMkdir) {
                    newFile.createNewFile();
                }
            }
        }

        return newFile;
    }

    /**
     * 获取扩展名
     * 为了方便处理比较,将文件名转为小写字母
     */
    private String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }

    /**
     * 判断视频或图片格式是否符合要求
     */
    private boolean isVideo(String extensionName) {
        String[] type = new String[] {"avi", "flv", "mp4", "png", "jpg", "jpeg"};
        for (String s : type) {
            if (s.equals(extensionName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isImg(String extensionName) {
        String[] type = new String[] {"png", "jpg", "jpeg"};
        for (String s : type) {
            if (s.equals(extensionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件相对路径,
     * 用于存储和前端访问
     */
    private String getRelativePath(String originalFilename, String extensionName) {
        // 生成文件夹
        char[] array = (originalFilename.hashCode() + "").toCharArray();
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < array.length && i < 4; i++) {
            stringBuffer.append("/" + array[i]);
        }
        stringBuffer.append("/").append(UUID.randomUUID().toString().replace("-", ""))
            .append(".").append(extensionName);
        return stringBuffer.toString();
    }
}
