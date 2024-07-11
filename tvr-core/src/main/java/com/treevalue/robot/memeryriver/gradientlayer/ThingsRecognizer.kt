package com.treevalue.robot.memeryriver.gradientlayer

import ai.djl.ndarray.NDArray
import com.treevalue.robot.data.DynamicArray
import com.treevalue.robot.data.Point
import com.treevalue.robot.data.constant.GlobalInfo
import com.treevalue.robot.ext.ThingData
import kotlin.math.floor

/*
visual thing recognize
 */
class ThingsRecognizer() {

    fun classify(pointsSetSet: ThingData, header: Header): Int {
        val statistics: StaticsData<DynamicArray<Pair<Int, Float>>> = statistics(pointsSetSet, header)
        return findMark(statistics)
    }

    private fun findMark(statistics: StaticsData<DynamicArray<Pair<Int, Float>>>): Int {
        TODO()
    }

    private fun statistics(
        pointsSetSet: HashMap<Int, DynamicArray<HashSet<Point<Int, Int>>>>, header: Header
    ): StaticsData<DynamicArray<Pair<Int, Float>>> {
        val sortMark: DynamicArray<Int> = getSort(header)
        val rst = StaticsData<DynamicArray<Pair<Int, Float>>>()
        for (mrk in sortMark) {
            statistics(pointsSetSet, rst)
        }
        return rst
    }

    private fun statistics(
        pointsSetSet: HashMap<Int, DynamicArray<HashSet<Point<Int, Int>>>>,
        result: StaticsData<DynamicArray<Pair<Int, Float>>>
    ) {
        val step = 64
        val deltaWidth = GlobalInfo.visualWidth / step.toFloat()
        val deltaHeight = GlobalInfo.visualHeight / step.toFloat()
        var cur = 0f
        val xStatics = HashMap<Int, Int>()
        while (cur <= GlobalInfo.visualWidth) {
            cur += deltaWidth
            xStatics[cur.toInt()] = 0
        }
        cur = 0f
        HashMap<Int, Int>()
        val yStatics = HashMap<Int, Int>()
        while (cur <= GlobalInfo.visualHeight) {
            cur += deltaHeight
            yStatics[cur.toInt()] = 0
        }
        cur = 0f
        var max = 0
        val tStatics = HashMap<Int, Int>()
        for (itm in pointsSetSet.values) {
            max = if (itm.size() > max) itm.size() else max
        }
        val tStep = max / step.toFloat()
        while (cur <= max) {
            cur += tStep
            tStatics[cur.toInt()] = 0
        }
        var scale = 0
        for (ctr in pointsSetSet.values.indices) {
            scale = getScale(ctr, tStep)
            tStatics[scale] = tStatics.getOrDefault(scale, 0)
            for (dArray in pointsSetSet.values) {
                for (set in dArray) {
                    for (pot in set) {
                        scale = getScale(pot.first, deltaWidth)
                        xStatics[scale] = xStatics.getOrDefault(scale, 0)
                        scale = getScale(pot.second, deltaHeight)
                        yStatics[scale] = yStatics.getOrDefault(scale, 0)
                    }
                }
            }
        }
        TODO("wait return statics info of a thing in 1 , 8, 64 three level")
    }

    private inline fun getScale(curPosition: Int, step: Float): Int {
        val division = curPosition / step
        return (if (division != division.toInt().toFloat()) floor(division + 1) * step else curPosition).toInt()
    }


    private fun getSort(header: Header): DynamicArray<Int> {
        val sorted = header.expandAll().sortedBy { it.second }
        val array = DynamicArray(Int::class.java)
        array.add(sorted.map { it.first }.toList())
        return array
    }

    fun update(mark: Int, rawData: NDArray) {

    }
}
