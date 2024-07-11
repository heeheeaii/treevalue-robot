/**
The hardships you have endured are your ultimate bulwark.
 */

package com.treevalue.robot.memeryriver.gradientlayer

import ai.djl.ndarray.NDArray
import com.treevalue.robot.data.Point
import java.util.*

class VisualCanvas(width: Int, height: Int) {
    private val MIN_RATE: Float = 0.8f
    var data: Array<Array<Int>>

    init {
        data = Array(width) { Array(height) { 0 } }
    }

    fun resetCanvas(width: Int, height: Int) {
        if (width != data.size || height != data[0].size) {
            data = Array(width) { Array(height) { 0 } }
        }
    }

    fun background(img: NDArray?) {
        img ?: return
        val shape = img.shape.shape
        require(shape[0] <= data.size && shape[1] <= data[0].size) { "img tensor shape don't match" }
        for (idx in 0 until shape[0]) {
            for (jdx in 0 until shape[1]) {
                data[idx.toInt()][jdx.toInt()] = img.getInt(idx, jdx, 1)//mark
            }
        }
    }

    fun predict(inners: Array<Point<Int, Int>>): Int? {
        if (inners.isEmpty()) return null
        val static: TreeMap<Int, Int> = TreeMap()
        var all: Int = 0
        var previewMark: Int
        inners.forEach {
            previewMark = data[it.first][it.second]
            static[previewMark] = static.getOrDefault(previewMark, 0)
            all++
        }
        val highest = static.lastKey()//biggest  number
        return if (all != 0 && highest / all.toFloat() > MIN_RATE)
            highest else null
    }
}

