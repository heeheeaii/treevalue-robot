package com.treevalue.robot.djl.transfer

import ai.djl.ndarray.NDArray
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.types.Shape


object TensorManager : AutoCloseable {
    private val ndManager: NDManager
    private val tensors: MutableMap<String, NDArray>

    init {
        ndManager = NDManager.newBaseManager()
        tensors = HashMap()
    }


    fun create(vararg lens: Long): NDArray {
        return create(Shape(lens.toList()))
    }

    fun create(shape: Shape?): NDArray {
        return ndManager.create(shape)
    }

    fun close(tensor: NDArray?) {
        tensor?.close()
        if (tensor in tensors.values) {
            for (itm in tensors.entries) {
                if (itm.value == tensor) {
                    tensors.remove(itm.key)
                    break
                }
            }
        }
    }

    override fun close() {
        for (tensor in tensors.values) {
            close(tensor)
        }
        tensors.clear()
        ndManager.close()
    }

    fun add(name: String, tensor: NDArray): NDArray {
        tensors[name] = tensor
        return tensor
    }

    fun close(name: String) {
        val tensor = tensors.remove(name)
        tensor?.close()
    }

    fun create(array: Array<FloatArray>): NDArray {
        return ndManager.create(array)
    }

    fun create(array: FloatArray): NDArray {
        return ndManager.create(array)
    }

    fun create(array: DoubleArray): NDArray {
        return ndManager.create(array)
    }

    fun getManager(): NDManager {
        return ndManager
    }


}
