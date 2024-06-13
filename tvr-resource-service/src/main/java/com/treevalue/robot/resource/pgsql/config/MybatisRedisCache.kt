package com.treevalue.robot.resource.pgsql.config

import com.treevalue.robot.resource.spring.ApplicationContextHolder
import org.apache.ibatis.cache.Cache
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock


class MybatisRedisCache(id: String?) : Cache {
    private val readWriteLock: ReadWriteLock = ReentrantReadWriteLock(true)
    private val id
            : String
    private var redisTemplate: RedisTemplate<Any, Any>? = null
        private get() {
            if (field == null) {
                field = ApplicationContextHolder.getBean(
                    "redisForJavaBean",
                    RedisTemplate::class.java
                ) as RedisTemplate<Any, Any>
            }
            return field
        }

    init {
        requireNotNull(id) { "Cache instances require an ID" }
        this.id = id
    }

    override fun getId(): String {
        return id
    }

    override fun putObject(key: Any, value: Any) {
        if (value != null) {
            redisTemplate!!.opsForValue()[key] = value
        }
    }

    override fun getObject(key: Any): Any {
        return redisTemplate!!.opsForValue()[key]
    }

    override fun removeObject(key: Any): Any {
        return redisTemplate!!.delete(key)
    }

    override fun clear() {
        redisTemplate!!.delete(redisTemplate!!.keys("*:$id*"))
    }

    override fun getSize(): Int {
        return redisTemplate!!.keys("*:$id*").size
    }

    override fun getReadWriteLock(): ReadWriteLock {
        return readWriteLock
    }
}

