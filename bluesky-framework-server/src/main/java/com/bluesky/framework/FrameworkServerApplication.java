package com.bluesky.framework;

import org.mvnsearch.spring.boot.dubbo.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * service application
 *
 * @author candy
 */
@SpringBootApplication
@EnableDubboConfiguration("com.bluesky.framework.server")
@MapperScan("com.bluesky.framework.domain.infrastructure.model")
@EnableAsync
public class FrameworkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameworkServerApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}