package com.treevalue.robot.tensor.tensor

import ai.djl.ndarray.NDArray
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.types.DataType
import ai.djl.ndarray.types.Shape
import com.treevalue.robot.tensor.transfer.TensorManager
import java.io.*
import java.nio.FloatBuffer

object TensorUtil {


    fun loadAsFloat(path: String, size: Int, shape: Shape, manager: NDManager = TensorManager.getManager()): NDArray? {
        try {
            val data = FloatArray(size)
            DataInputStream(FileInputStream(path)).use { dis ->
                for (i in data.indices) {
                    data[i] = dis.readFloat()
                }
            }
            return manager.create(data, shape)
        } catch (e: IOException) {
            System.err.println("Error while loading tensor data: " + e.message)
        }
        return null
    }

    fun saveAsFloat(path: String, array: NDArray) {
        try {
            DataOutputStream(FileOutputStream(path)).use { dos ->
                val data = array.toFloatArray()
                for (v in data) {
                    dos.writeFloat(v)
                }
            }
        } catch (e: IOException) {
            System.err.println("Error while saving tensor data: " + e.message)
        }
    }

    fun saveAsObj(outPath: String, tensor: NDArray) {
        FileOutputStream(outPath).use { fileOut ->
            ObjectOutputStream(fileOut).use { out ->
                out.writeObject(tensor.toFloatArray())
                out.writeObject(tensor.shape.shape)
                out.writeObject(tensor.dataType.name)
            }
        }
    }

    fun loadAsObj(inputPath: String, manager: NDManager = TensorManager.getManager()): NDArray {
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
