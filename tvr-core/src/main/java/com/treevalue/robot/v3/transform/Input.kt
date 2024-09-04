package com.treevalue.robot.v3.transform

import com.treevalue.robot.v3.other.data.Tensor

interface Input {
    fun getSensors():ArrayList<Tensor<Int>>
}
