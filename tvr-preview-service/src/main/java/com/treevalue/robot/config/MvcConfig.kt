package com.treevalue.robot.config
import jakarta.servlet.MultipartConfigElement
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.unit.DataSize

@Configuration
open class MvcConfig {

    @Bean
    open fun multipartConfigElement(): MultipartConfigElement {
        val factory = MultipartConfigFactory()
        factory.setMaxFileSize(DataSize.ofGigabytes(5))
        factory.setMaxRequestSize(DataSize.ofGigabytes(5))
        return factory.createMultipartConfig()
    }
}
