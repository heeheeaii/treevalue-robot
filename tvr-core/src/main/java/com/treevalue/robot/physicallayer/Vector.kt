package com.treevalue.robot.physicallayer

import ai.djl.ndarray.NDArray

class Vector(private val sensorySource: TensorReadable<Array<NDArray>>) {}
