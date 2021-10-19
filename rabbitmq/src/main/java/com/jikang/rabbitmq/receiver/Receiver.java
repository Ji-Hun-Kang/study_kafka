package com.jikang.rabbitmq.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    /**
     * 메시지 수신 시 처리할 Handler 함수
     * @param message RabbitMQ의 test-queue-1으로부터 수신한 메시지
     */
    @RabbitListener(containerFactory = "SampleContainerFactory", queues="test-queue-1")
    public void onReceiveMessage(SampleMessage message){
        LOGGER.info("Receiver received message:" + message);
    }
}
