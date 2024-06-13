package com.treevalue.robot.data

import java.util.concurrent.locks.ReentrantLock


open class CircularBuffer<T>(capacity: Int) {
    private val buffer: Array<T?>
    private var head = 0
    private var tail = 0
    private var size = 0
    private val lock: ReentrantLock

    init {
        buffer = arrayOfNulls<Any>(capacity) as Array<T?>
        lock = ReentrantLock()
    }

    fun push(item: T) {
        lock.lock()
        try {
            buffer[head] = item
            head = (head + 1) % buffer.size
            if (size < buffer.size) {
                size++
            } else {
                tail = (tail + 1) % buffer.size // Overwrite the oldest data
            }
        } finally {
            lock.unlock()
        }
    }

    fun pop(): T? {
        lock.lock()
        return try {
            if (size == 0) {
                return null // Buffer is empty
            }
            val item = buffer[tail]
            buffer[tail] = null // Help GC
            tail = (tail + 1) % buffer.size
            size--
            item
        } finally {
            lock.unlock()
        }
    }

    fun getSize(): Int {
        lock.lock()
        return try {
            size
        } finally {
            lock.unlock()
        }
    }

    val isEmpty: Boolean
        get() {
            lock.lock()
            return try {
                size == 0
            } finally {
                lock.unlock()
            }
        }
    val isFull: Boolean
        get() {
            lock.lock()
            return try {
                size == buffer.size
            } finally {
                lock.unlock()
            }
        }
}

