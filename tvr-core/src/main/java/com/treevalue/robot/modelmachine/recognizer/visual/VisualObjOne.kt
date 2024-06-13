package com.treevalue.robot.modelmachine.recognizer.visual

import com.treevalue.robot.modelmachine.recognizer.visual.util.ConversionUtil
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class VisualObjOne(
    val mark: String = Long.MIN_VALUE.toString(),
    var averageFeature: String,
    val bound: MutableList<Long>,
    val sparseInnerPoints: MutableList<Long>,
) {
    var allPointNumber = AtomicLong(0)
    val allRGBCounter = IntArray(3)
    private val SPARSE_RATIO = 0.01

    private val random = Random()
    fun XYToOneNumber(x: Int, y: Int) {
    }

    fun canPutToSparsePoints(): Boolean {
        return random.nextDouble() < SPARSE_RATIO
    }

    fun getXY(positionLong: Long):IntArray {
        return ConversionUtil.longToXY(positionLong)
    }
}
