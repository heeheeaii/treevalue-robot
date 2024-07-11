package com.treevalue.robot.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

class ThresholdContainer<T>(private val threshold: Int, private val consumer: Consumer<ArrayList<T>>) {
    private val queue = ConcurrentLinkedQueue<T>()
    private val count = AtomicInteger(0)

    fun add(element: T) {
        queue.add(element)
        val currentCount = count.incrementAndGet()
        if (currentCount >= threshold) {
            synchronized(queue) {
                if (count.get() >= threshold) {
                    val tmp = ArrayList<T>()
                    for (idx in 0 until count.get()) {
                        queue.poll()?.let { tmp.add(it) }
                    }
                    count.set(count.get() - threshold)
                    CoroutineScope(Dispatchers.Default).launch {
                        consumer.accept(tmp)
                    }
                }
            }
        }
    }
}
