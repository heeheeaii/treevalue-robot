package com.treevalue.robot.resource.remote.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class RemoteConfig {
//    @LoadBalanced()
    @Bean
    open fun restClient(): RestTemplate {
        return RestTemplate()
    }


}
