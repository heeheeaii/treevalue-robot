package com.treevalue.robot.entropyreducer.rememberlack.io

import java.io.File
import kotlin.reflect.KClass

object DataFileUtils {
    fun <T> writeArray(data: Array<T>, filePath: String) {
        //    Only 8 basic data types are supported
        val file = File(filePath)
        file.bufferedWriter().use { out ->
            data.forEach { out.write("$it\n") }
        }
    }

    fun <T> writeArray(data: Array<T>, file: File) {
        //    Only 8 basic data types are supported
        file.bufferedWriter().use { out ->
            data.forEach { out.write("$it\n") }
        }
    }

    inline fun <reified T : Any> readArray(filePath: String, type: KClass<T>): Array<T> {
        //    Only 8 basic data types are supported
        val file = File(filePath)
        val list = mutableListOf<T>()

        file.bufferedReader().useLines { lines ->
            lines.forEach {
                list.add(convertToType(it, type))
            }
        }
        return list.toTypedArray()
    }

    fun <T : Any> convertToType(value: String, type: KClass<T>): T {
        @Suppress("UNCHECKED_CAST") return when (type) {
            Byte::class -> value.toByte() as T
            Short::class -> value.toShort() as T
            Int::class -> value.toInt() as T
            Long::class -> value.toLong() as T
            Float::class -> value.toFloat() as T
            Double::class -> value.toDouble() as T
            Char::class -> value.single() as T
            Boolean::class -> value.toBoolean() as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    fun <T> printArray(array: Array<T>, keep: Int = -1) {
        array.forEach {
            if (keep != -1) {
                print("%.${keep}f".format(it))
            } else {
                print(it)
            }
            print(", ")
        }
    }
}
