package com.bluesky.framework.api.rabbitMQ.listener;

import com.bluesky.framework.api.rabbitMQ.entity.Mail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QueueListener2 {

    @RabbitListener(queues = "myqueue")
    public void displayMail(Mail mail) throws Exception {
        System.out.println("队列监听器2号收到消息"+mail.toString());
    }
}
