package com.treevalue.robot.v3.transform.impl

import com.treevalue.robot.v3.other.errorInf.ErrorInfo
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class RandomTensorGen {
    fun <T : Number> getTensor(
        container: AtomicReference<List<T>>,
        micSec: Long,
        from: Int = 0,
        to: Int = 1000,
        vararg shape: Int,
    ): ErrorInfo {
        try {
            1 as T
        } catch (e: Exception) {
            return ErrorInfo.InputTypeError
        }
        var sum = 1
        for (it in shape) {
            sum *= it
        }
        if (sum <= 0) return ErrorInfo.InputRangeError
        val rst = ArrayList<T>(sum)
        val rdm = Random()

        for (it in 0 until sum) {
            rst.add(rdm.nextInt(from, to) as T)
        }
        return ErrorInfo.OK
    }
}
