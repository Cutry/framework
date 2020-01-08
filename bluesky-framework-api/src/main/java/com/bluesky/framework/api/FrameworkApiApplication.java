package com.bluesky.framework.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 启动类
 */
@EnableAsync
@SpringBootApplication
@EnableWebSecurity
@EnableScheduling
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class FrameworkApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameworkApiApplication.class, args);
    }
}
