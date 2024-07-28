package com.treevalue.robot.v2.data

import com.treevalue.robot.stringBuilder.data.Tensor
import java.util.concurrent.atomic.AtomicInteger

class Axios<T> {
    private val time: Long
        get() {
            return System.currentTimeMillis();
        }
    private val lock: AtomicInteger = AtomicInteger(2)
    private val dataHolder: HashMap<Long, ArrayList<MutableList<Tensor<T>>>> = HashMap()
    private var maxIndex: Int = 0;

    fun getIndex(data: Tensor<T>) {

    }

    fun add(idx: Int, data: Tensor<T>) {
        val data = dataHolder.getOrDefault(time, ArrayList())
        data.set(idx)
        dataHolder[time] =
    }

}
