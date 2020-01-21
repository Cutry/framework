package com.bluesky.framework.api.rabbitMQ.listener;

import com.bluesky.framework.api.mongodb.MongoDao;
import com.bluesky.framework.api.mongodb.MongoTest;
import com.bluesky.framework.api.rabbitMQ.entity.Mail;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "myqueue")
public class QueueListener1 {

    @Autowired
    private MongoDao mongoDao;

    @RabbitHandler
    public void displayMail(Mail mail, Channel channel, Message message) throws Exception {
        System.out.println("队列监听器1号收到消息" + mail.toString() + "; channel : " + channel + "; message : "+ message);
        MongoTest test = MongoTest.builder()
                .id(Integer.parseInt(mail.getMailId()))
                .age(25)
                .build();
        mongoDao.updateTest(test);
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//如果需要确认的要调用
    }
}