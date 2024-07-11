package com.treevalue.robot.memeryriver.physicallayer

import ai.djl.ndarray.NDArray
import com.treevalue.robot.data.OrderedConcurrentMap
import kotlin.concurrent.withLock

final class PhysicalLayer(maxCapacity: Long = 1024) : OrderedConcurrentMap<Long, Array<NDArray>>(maxCapacity),
    TensorReadable<Array<NDArray>> {
    fun push(value: Array<NDArray>) {

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

    fun push(audio: NDArray, visual: NDArray) {
        push(arrayOf(audio, visual))
    }
}
