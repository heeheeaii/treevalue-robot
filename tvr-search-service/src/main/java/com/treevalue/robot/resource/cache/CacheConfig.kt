package com.treevalue.robot.resource.cache

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit


@Configuration
@EnableCaching
open class CacheConfig {
    @Bean
    open fun cacheManager(): CacheManager {
        val caffeineCacheBuilder = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10000)
        val cacheManager = CaffeineCacheManager()
        cacheManager.setCaffeine(caffeineCacheBuilder)
        return CaffeineCacheManager()
    }
}

