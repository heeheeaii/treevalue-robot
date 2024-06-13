package com.treevalue.robot.user.web.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
open class WebConfig {
    @Bean
    open fun requestLoggingFilter(): CommonsRequestLoggingFilter {
        val loggingFilter = CommonsRequestLoggingFilter()
        loggingFilter.setIncludePayload(true)
        loggingFilter.setIncludeHeaders(true)
        loggingFilter.setMaxPayloadLength(1000)
        loggingFilter.setAfterMessagePrefix("REQ:")
        return loggingFilter
    }
}
