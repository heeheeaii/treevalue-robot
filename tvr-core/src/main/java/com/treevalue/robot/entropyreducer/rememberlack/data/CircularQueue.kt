package com.treevalue.robot.entropyreducer.rememberlack.data

class CircularQueue<T>(capacity: Int) {
    private val items: Array<T?>
    private var head = 0
    private var tail = 0
    private var size = 0

    init {
        items = arrayOfNulls<Any>(capacity) as Array<T?>
    }

    fun get(idx: Int): T? {
        if (idx !in head..tail) return null
        return items[(idx + items.size) % items.size]
    }

    fun addFirst(item: T): T? {
        if (isFull) {
            val res = removeLast()
            addFirst(item)
            return res
        }
        head = (head - 1 + items.size) % items.size
        items[head] = item
        size++
        return null
    }

    fun addLast(item: T): T? {
        if (isFull) {
            val res = removeFirst()
            addLast(item)
            return res
        }
        items[tail] = item
        tail = (tail + 1) % items.size
        size++
        return null
    }

    fun removeFirst(): T? {
        if (isEmpty) return null
        val item = items[head]
        items[head] = null
        head = (head + 1) % items.size
        size--
        return item
    }

    fun removeLast(): T? {
        if (isEmpty) return null
        tail = (tail - 1 + items.size) % items.size
        val item = items[tail]
        items[tail] = null
        size--
        return item
    }

    val first: T?
        get() = if (!isEmpty) items[head] else null

    val last: T?
        get() = if (!isEmpty) items[(tail - 1 + items.size) % items.size] else null

    val isEmpty: Boolean
        get() = size == 0

    val isFull: Boolean
        get() = size == items.size

    fun size(): Int {
        return size
    }
}

