package com.treevalue.robot.resource.pgsql.config

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer
import com.baomidou.mybatisplus.core.MybatisConfiguration
import com.treevalue.robot.resource.pgsql.adaptor.TextArrayTypeHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class MyTypeHandlerConfiguration {
    @Bean
    open fun typeHandlerRegistry(): ConfigurationCustomizer {
        return ConfigurationCustomizer { configuration: MybatisConfiguration ->
            configuration.typeHandlerRegistry.register(
                TextArrayTypeHandler::class.java
            )
        }
    }
}
