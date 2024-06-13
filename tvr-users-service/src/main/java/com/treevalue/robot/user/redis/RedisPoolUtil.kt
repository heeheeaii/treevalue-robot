package com.treevalue.robot.user.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisHashCommands
import io.lettuce.core.api.sync.RedisListCommands
import io.lettuce.core.api.sync.RedisSetCommands
import io.lettuce.core.support.ConnectionPoolSupport
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import java.time.Duration

object RedisPoolUtil {

    private val redisClient: RedisClient = RedisClient.create("redis://localhost:6379")
    private val poolConfig: GenericObjectPoolConfig<StatefulRedisConnection<String, String>> = GenericObjectPoolConfig()
    private val connectionPool: GenericObjectPool<StatefulRedisConnection<String, String>>

    init {
        poolConfig.maxTotal = 100
        poolConfig.maxIdle = 50
        poolConfig.minIdle = 10
        poolConfig.testOnBorrow = true
        poolConfig.testOnReturn = true
        poolConfig.testWhileIdle = true
        poolConfig.blockWhenExhausted = true
        poolConfig.timeBetweenEvictionRuns = Duration.ofSeconds(30)
        poolConfig.minEvictableIdleTime = Duration.ofSeconds(60)
        poolConfig.setMaxWait(Duration.ofSeconds(3))
        connectionPool = ConnectionPoolSupport.createGenericObjectPool({ redisClient.connect() }, poolConfig)
    }

    fun getConnection(): StatefulRedisConnection<String, String> {
        return connectionPool.borrowObject()
    }

    fun returnConnection(connection: StatefulRedisConnection<String, String>) {
        try {
            connectionPool.returnObject(connection)
        } catch (e: Exception) {
            connection.close()
            connectionPool.invalidateObject(connection)
        }
    }

    fun getHashCommands(): RedisHashCommands<String, String> {
        return getConnection().sync() as RedisHashCommands<String, String>
    }

    fun getListCommands(): RedisListCommands<String, String> {
        return getConnection().sync() as RedisListCommands<String, String>
    }

    fun getSetCommands(): RedisSetCommands<String, String> {
        return getConnection().sync() as RedisSetCommands<String, String>
    }

    fun shutdown() {
        connectionPool.clear()
        connectionPool.close()
        redisClient.shutdown()
    }
}
