package com.bluesky.framework.api.rabbitMQ.controller;

import com.bluesky.framework.api.rabbitMQ.entity.Mail;
import com.bluesky.framework.api.rabbitMQ.service.Producer;
import com.bluesky.framework.api.rabbitMQ.service.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private Producer producer;
    @Autowired
    private Publisher publisher;

    @RequestMapping("rabbit")
    public void rabbit(){
        Mail mail = Mail.builder()
                .country("country")
                .mailId("mailId")
                .weight(2.0)
                .build();
        producer.sendMail("myqueue", mail);
    }
}
