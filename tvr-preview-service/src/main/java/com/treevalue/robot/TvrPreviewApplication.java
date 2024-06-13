package com.treevalue.robot;

import com.mongodb.client.MongoClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@EnableDiscoveryClient
public class TvrPreviewApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(TvrPreviewApplication.class).run(args);
        MongoClient bean = context.getBean(MongoClient.class);
    }
}
