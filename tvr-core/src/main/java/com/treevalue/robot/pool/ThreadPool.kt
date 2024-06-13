package com.treevalue.robot.pool

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

object ThreadPool {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(2)
    fun getScheduler(): ScheduledExecutorService {
        return scheduler
    }
}
