package top.maof.haikang.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.maof.haikang.controller.WebSocketController;
import top.maof.haikang.model.DetectedFile;
import top.maof.haikang.service.DetectedFileService;

/**
 * 监听python发的消息
 *
 * @author 毛逢
 */
@Component
@Slf4j
public class PythonListener {
    public static final String PYTHON_DETECTION_REPLY_QUEUE = "python_detection_reply_queue";

    @Autowired
    DetectedFileService detectedFileService;

    @RabbitListener(queues = {PYTHON_DETECTION_REPLY_QUEUE})
    public void msg(String msg) {
        log.info(msg);
        DetectionMessage detectionMessage = JSON.parseObject(msg, DetectionMessage.class);
        DetectedFile detectedFile = detectionMessage.detectedFile();
        detectedFileService.insert(detectedFile);
        WebSocketController.send(detectionMessage.getToken(),"检测成功");
    }
}
