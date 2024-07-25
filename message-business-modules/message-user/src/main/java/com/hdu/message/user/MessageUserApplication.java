package com.hdu.message.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.hdu.message.manager.api")
@MapperScan("com.hdu.message.user.mapper")
public class MessageUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageUserApplication.class, args);
    }
}
