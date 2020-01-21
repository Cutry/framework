package com.bluesky.framework.api.rabbitMQ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicMail extends Mail {

    String routingkey;
}
