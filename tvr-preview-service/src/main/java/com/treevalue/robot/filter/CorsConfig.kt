package com.treevalue.robot.filter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
open class CorsConfig {
    @Bean
    open fun corsFilter(): CorsWebFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.setAllowedOrigins(arrayListOf("*"))
        config.addAllowedMethod("*")
        config.addAllowedHeader("*")
        source.registerCorsConfiguration("/**", config)
        return CorsWebFilter(source)
    }
}
