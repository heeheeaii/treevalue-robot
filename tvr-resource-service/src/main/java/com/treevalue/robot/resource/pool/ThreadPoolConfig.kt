package com.treevalue.robot.resource.pool

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor


@Configuration
open class ThreadPoolConfig {
    private val CORE_SIZE = 7

    private val MAX_SIZE = 25

    private val THREAD_NAMEPREFIX = "tvr_"

    @Bean
    open fun taskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = CORE_SIZE
        executor.maxPoolSize = MAX_SIZE
        executor.queueCapacity = 500
        executor.keepAliveSeconds = 5
        executor.threadNamePrefix = THREAD_NAMEPREFIX
        executor.initialize()
        return executor
    }
}
