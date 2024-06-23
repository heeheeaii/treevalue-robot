package com.treevalue.robot.pool

import java.util.concurrent.*


object ThreadPool {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(2)
    fun getScheduler(): ScheduledExecutorService {
        return scheduler
    }

    var transformThreadPool: ExecutorService
    init {
        val corePoolSize = 5
        val maximumPoolSize = 15
        val keepAliveTime = 1L
        val unit = TimeUnit.MINUTES
        val workQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(50)
        val handler: RejectedExecutionHandler = ThreadPoolExecutor.DiscardOldestPolicy()
        transformThreadPool = ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            workQueue,
            handler
        )
    }
}
