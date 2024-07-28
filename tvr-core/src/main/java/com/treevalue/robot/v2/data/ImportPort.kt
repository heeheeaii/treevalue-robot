package com.treevalue.robot.v2.data

import com.treevalue.robot.data.DynamicArray
import com.treevalue.robot.stringBuilder.data.Tensor

class ImportPort {
    private val shapes: LinkedHashMap<Int, Int> = LinkedHashMap()

    fun input(input: DynamicArray<Tensor<Int>>) {
        for (itm in input) {
            itm.shape
        }
    }
}
