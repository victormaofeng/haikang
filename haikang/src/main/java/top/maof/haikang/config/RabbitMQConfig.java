package top.maof.haikang.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类,包括
 * exchange交换机名称、路由绑定规则、
 * queue 队列名称、路由名称、
 * 消息可靠投递保障等配置
 *
 * @author 毛逢
 * @date 2021/9/16 9:38
 */
@Configuration
public class RabbitMQConfig {

    // 目标检测交换机名称
    public static final String DETECTION_TOPIC_EXCHANGE = "detection_topic_exchange";

    // 目标检测队列名称
    public static final String DETECTION_QUEUE = "detection_queue";

    // 目标检测路由名称
    public static final String DETECTION_ROUTING_KEY = "detection.route";

    // 行人重识别交换机名称
    public static final String REID_TOPIC_EXCHANGE = "reid_topic_exchange";

    // 行人重识别队列名称
    public static final String REID_QUEUE = "reid_queue";

    // 行人重识别路由名称
    public static final String REID_ROUTING_KEY = "reid.route";

    //声明交换机
    @Bean("detectionTopicExchange")
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange(DETECTION_TOPIC_EXCHANGE).durable(true).build();
    }

    //声明队列
    @Bean("detectionQueue")
    public Queue itemQueue() {
        return QueueBuilder.durable(DETECTION_QUEUE).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding itemQueueExchange(@Qualifier("detectionQueue") Queue queue,
                                     @Qualifier("detectionTopicExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("detection.#").noargs();
    }


    @Bean("reidTopicExchange")
    public Exchange reidTopicExchange() {
        return ExchangeBuilder.topicExchange(REID_TOPIC_EXCHANGE).durable(true).build();
    }

    //声明队列
    @Bean("reidQueue")
    public Queue reidQueue() {
        return QueueBuilder.durable(REID_QUEUE).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding reidQueueExchange(@Qualifier("reidQueue") Queue queue,
                                     @Qualifier("reidTopicExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("reid.#").noargs();
    }


    // 配置发送确认,以防消息在发送过程中失败丢失
   /* @Bean
    public RabbitTemplate rabbitTemplate(RabbitTemplate rabbitTemplate) {

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {

            }
        });

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {

            }
        });

        return rabbitTemplate;
    }*/
}
