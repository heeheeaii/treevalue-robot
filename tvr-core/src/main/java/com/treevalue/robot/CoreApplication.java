package com.treevalue.robot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CoreApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CoreApplication.class).run(args);
    }
}
