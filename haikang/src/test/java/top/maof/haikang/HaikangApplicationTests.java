package top.maof.haikang;

import com.sun.org.apache.xml.internal.utils.Hashtree2Node;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.maof.haikang.api.HaikangApi;
import top.maof.haikang.config.RabbitMQConfig;
import top.maof.haikang.model.Developer;
import top.maof.haikang.utils.FFMPEGUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
@Slf4j
class HaikangApplicationTests {
    @Autowired
    FFMPEGUtil ffmpegUtil;

    @Autowired
    HaikangApi haikangApi;

    @Autowired
    RabbitTemplate rabbitTemplate;


    // @Test
    void test1() {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DETECTION_TOPIC_EXCHANGE,
                RabbitMQConfig.DETECTION_ROUTING_KEY, "haha");
    }


    // @Test
    void test() {
        Developer developer = haikangApi.getAccessToken("09205b3fc5494b0aafbc56fdd3241f3c",
                "b52c4e0f37f0bf035c866b216ab73033");
        log.info(developer.toString());
        HaikangApi.Live live = haikangApi.getLive(developer.getAccessToken(), "F02718370", HaikangApi.PROTOCOL_RTMP, HaikangApi.QUALITY_HD);
        log.info(live.toString());
    }


    // @Test
    void contextLoads() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ipconfig");
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);
        try {
            //启动进程
            Process start = processBuilder.start();
            //获取输入流
            InputStream inputStream = start.getInputStream();
            //转成字符输入流
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
            int len = -1;
            char[] c = new char[1024];
            StringBuffer outputString = new StringBuffer();
            //读取进程输入流中的内容
            while ((len = inputStreamReader.read(c)) != -1) {
                String s = new String(c, 0, len);
                outputString.append(s);
                System.out.print(s);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
