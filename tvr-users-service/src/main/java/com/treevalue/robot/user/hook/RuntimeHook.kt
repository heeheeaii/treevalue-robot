package com.treevalue.robot.user.hook

import com.treevalue.robot.user.redis.RedisPoolUtil

object RuntimeHook {
    fun shutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread {
            RedisPoolUtil.shutdown()
        })
    }
}
