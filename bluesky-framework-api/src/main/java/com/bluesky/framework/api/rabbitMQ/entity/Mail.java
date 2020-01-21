package com.bluesky.framework.api.rabbitMQ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail implements Serializable {

    private String mailId;
    private String country;
    private Double weight;

}
