package com.treevalue.robot.djl.tensor

import ai.djl.ndarray.NDArray
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.types.DataType
import ai.djl.ndarray.types.Shape
import com.treevalue.robot.djl.transfer.TensorManager
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.FloatBuffer

object TensorUtil {
    fun save(outPath: String, tensor: NDArray) {
        FileOutputStream(outPath).use { fileOut ->
            ObjectOutputStream(fileOut).use { out ->
                out.writeObject(tensor.toFloatArray())
                out.writeObject(tensor.shape.shape)
                out.writeObject(tensor.dataType.name)
            }
        }
    }

    fun load(inputPath: String, manager: NDManager = TensorManager.getManager()): NDArray {
        val tensor: NDArray
        FileInputStream(inputPath).use { fileIn ->
            ObjectInputStream(fileIn).use { `in` ->
                val data = `in`.readObject() as FloatArray
                val shapeArray = `in`.readObject() as LongArray
                val dataType = `in`.readObject() as String
                val shape = Shape(*shapeArray)
                val dtype = DataType.valueOf(dataType)
                val buffer: FloatBuffer = FloatBuffer.wrap(data)
                tensor = manager.create(buffer, shape, dtype)
            }
        }
        return tensor

    }
}
