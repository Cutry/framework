package com.bluesky.framework.api.rabbitMQ.service;

import com.bluesky.framework.api.rabbitMQ.entity.Mail;

public interface Producer {

    public void sendMail(String queue, Mail mail);
}
