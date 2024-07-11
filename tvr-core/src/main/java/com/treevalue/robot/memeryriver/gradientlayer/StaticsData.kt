package com.treevalue.robot.memeryriver.gradientlayer

import com.treevalue.robot.data.DynamicArray

class StaticsData<T : Any> {
    var step = 0
    lateinit var static: DynamicArray<T>
}
