package com.treevalue.robot.v2.data

import com.treevalue.robot.stringBuilder.data.Tensor
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class Axios<T> {
    private val register: ConcurrentHashMap<String, Int> = ConcurrentHashMap()
    private val timeAutoChange: Long
        get() {
            return System.currentTimeMillis();
        }

    private val lock: AtomicInteger = AtomicInteger(2)
    private val dataHolder: HashMap<Long, LinkedList<Tensor<T>>> = HashMap()
    private var maxIndex: Int = -1;

    private fun getIndex(): Int {
        maxIndex += 1
        return maxIndex
    }

    private fun decIndex() {
        maxIndex -= 1
    }

    fun add(idx: Int, senseData: Tensor<T>) {
        var timeNow = timeAutoChange
        val data = dataHolder.getOrDefault(timeNow, LinkedList())
        val sen = data.getOrNull(idx)
        if (sen?.shape == senseData.shape) {
            throw Exception("one mill second only must add a shape of data into a position")
        }
        data.add(idx, senseData)
        dataHolder[timeNow] = data
    }

    fun register(id: String): Int {
        var idx = register[id]
        if (idx == null) {
            idx = getIndex()
            register[id] = idx
        }
        return idx
    }

    fun push(id: String, data: Tensor<T>) {
        val idx = register[id] ?: throw Exception("don't register")
        add(idx, data)
    }

    companion object {
        fun test1() {
            val a = Axios<Int>()
            val t = Tensor<Int>(listOf(1, 2, 3), mutableListOf(1, 2, 3, 4, 5, 6))
            val t2 = Tensor<Int>(listOf(1, 3, 3), mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
            a.add(0, t)
            a.add(0, t2)
            println()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val a = Axios<Int>()
            val t = Tensor<Int>(listOf(1, 2, 3), mutableListOf(1, 2, 3, 4, 5, 6))
            val t2 = Tensor<Int>(listOf(1, 3, 3), mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
            val id = "ttt"
            a.register(id)
            a.push(id, t)
            a.push(id, t2)
            println()
        }
    }

}
