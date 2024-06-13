package com.treevalue.robot.data

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

open class OrderedConcurrentMap<K, V>(protected val maxCapacity: Long) : Iterable<Map.Entry<K, V>> {
    protected val map = LinkedHashMap<K, V>()
    protected val readLock = ReentrantLock()
    protected val writeLock = ReentrantLock()
    protected var currentWriteKey: K? = null

    fun put(key: K, value: V) {
        writeLock.withLock {
            currentWriteKey = key
            try {
                if (map.size >= maxCapacity) {
                    val firstKey = map.firstEntry().key
                    map.remove(firstKey)
                }
                map[key] = value
            } finally {
                currentWriteKey = null
            }
        }
    }

    fun get(key: K): V? {
        while (true) {
            readLock.withLock {
                if (currentWriteKey != key) {
                    return map[key]
                }
            }
        }
    }

    fun remove(key: K): V? {
        writeLock.withLock {
            currentWriteKey = key
            try {
                return map.remove(key)
            } finally {
                currentWriteKey = null
            }
        }
    }

    fun containsKey(key: K): Boolean {
        while (true) {
            readLock.withLock {
                if (currentWriteKey != key) {
                    return map.containsKey(key)
                }
            }
        }
    }

    fun keys(): List<K> {
        while (true) {
            readLock.withLock {
                if (currentWriteKey == null) {
                    return map.keys.toList()
                }
            }
        }
    }

    fun values(): List<V> {
        while (true) {
            readLock.withLock {
                if (currentWriteKey == null) {
                    return map.values.toList()
                }
            }
        }
    }

    fun entries(): List<Map.Entry<K, V>> {
        while (true) {
            readLock.withLock {
                if (currentWriteKey == null) {
                    return map.entries.toList()
                }
            }
        }
    }

    override fun iterator(): Iterator<Map.Entry<K, V>> {
        while (true) {
            readLock.withLock {
                if (currentWriteKey == null) {
                    return map.entries.iterator()
                }
            }
        }
    }

    fun last(): V? {
        writeLock.withLock {
            currentWriteKey = map.entries.lastOrNull()?.key
            try {
                return map.entries.lastOrNull()?.let {
                    map.remove(it.key)
                    it.value
                }
            } finally {
                currentWriteKey = null
            }
        }
    }
}
