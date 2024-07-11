package com.treevalue.robot.memeryriver

import io.opentelemetry.instrumentation.api.internal.cache.concurrentlinkedhashmap.ConcurrentLinkedHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantReadWriteLock

object MemoryRiver {
    private var last: AtomicLong = AtomicLong(0)
    private val rawMemory: ConcurrentLinkedHashMap<Long, TimeSlice> =
        ConcurrentLinkedHashMap.Builder<Long, TimeSlice>().build()
    private val headers: ConcurrentLinkedHashMap<Long, Thing> = ConcurrentLinkedHashMap.Builder<Long, Thing>().build()
    private val lock = ReentrantReadWriteLock()

    fun pre(): TimeSlice {
        lock.readLock().lock()
        try {
            val lastKey = last.get()
            return rawMemory[lastKey] ?: run {
                lock.readLock().unlock()
                lock.writeLock().lock()
                try {
                    val newLastKey = rawMemory.keys.lastOrNull() ?: 0L
                    last.set(newLastKey)
                    rawMemory[newLastKey] ?: throw NoSuchElementException("No elements in rawMemory")
                } finally {
                    lock.writeLock().unlock()
                    lock.readLock().lock()
                }
            }
        } finally {
            lock.readLock().unlock()
        }
    }

    fun push(timeSlice: TimeSlice) {
        lock.writeLock().lock()
        try {
            val time = System.currentTimeMillis()
            rawMemory[time] = timeSlice
        } finally {
            lock.writeLock().unlock()
        }
    }
}
