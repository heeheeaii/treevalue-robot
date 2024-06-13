package com.treevalue.robot.physicallayer

import ai.djl.ndarray.NDArray
import com.treevalue.robot.data.OrderedConcurrentMap
import kotlin.concurrent.withLock

final class PhysicalLayer(maxCapacity: Long = 1024) : OrderedConcurrentMap<Long, NDArray>(maxCapacity) {
    fun push(value: NDArray) {

        writeLock.withLock {
            val nano = System.nanoTime()
            currentWriteKey = nano
            try {
                if (map.size >= maxCapacity) {
                    val firstKey = map.keys.iterator().next()
                    map.remove(firstKey)
                }
                map[nano] = value
            } finally {
                currentWriteKey = null
            }
        }
    }
}
