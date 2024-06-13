package com.treevalue.robot.gateway.fallback.filter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
open class CorsConfig {
    @Bean
    open fun corsFilter(): CorsWebFilter {
        // 创建一个基于URL的Cors配置源
        val source = UrlBasedCorsConfigurationSource()

        val config = CorsConfiguration()

        // 允许任何源（域名或协议+域名）发起跨域请求
        config.addAllowedOrigin("http://localhost:5173")

        // 允许所有的请求头通过
        config.addAllowedHeader("*")

        // 允许所有的HTTP方法（GET, POST, PUT, DELETE等）
        config.addAllowedMethod("*")

        // 将此Cors配置应用到配置源中，对所有路径生效
        source.registerCorsConfiguration("/**", config)

        config.allowCredentials = true
        config.maxAge = 1800

        return CorsWebFilter(source)
    }
}
