package com.treevalue.robot.resource.es8.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class GsonConfig {
    @Bean
    open fun create(): Gson {
        return GsonBuilder().create()
    }
}
