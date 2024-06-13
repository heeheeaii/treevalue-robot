package com.treevalue.robot.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.treevalue.robot.user.mybatis.repository")
public class UsersApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(UsersApplication.class).run(args);
    }

}
