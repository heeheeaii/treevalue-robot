package com.treevalue.robot.test

import ai.djl.ndarray.NDArray
import ai.djl.ndarray.types.DataType
import ai.djl.ndarray.types.Shape
import com.treevalue.robot.data.Point
import com.treevalue.robot.data.tree.QuadTree
import com.treevalue.robot.data.tree.Rectangle
import com.treevalue.robot.ext.mergeSort
import com.treevalue.robot.tensor.transfer.TensorManager
import java.awt.image.BufferedImage
import java.lang.Math.sqrt
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

class ImageComparator {
    private val quadTree = QuadTree<Double>(Rectangle(-1.0, -1.0, 2.0, 2.0))
    private val colorTransformer: ColorSummaryTransformer = ColorSummaryTransformer()
    private var dx = 0.0
    private var dy = 0.0
    private var baseSet: Array<Point<Double, Double>>? = null
    private val otherSet = HashSet<String>()

    companion object {
        fun <T : Number> double(t: T): Double {
            return t.toDouble()
        }


        fun colorSummary(allColor: List<Triple<Int, Int, Int>?>, step: Double): Array<Map.Entry<String, Int>> {
            val lens = ArrayList<Double>()
            allColor.forEach {
                lens.add(double(it!!.first).pow(2.0) + double(it.second).pow(2.0) + double(it.third).pow(2.0))
            }
            return entries(lens, step)
        }

        private fun entries(
            lens: ArrayList<Double>, step: Double
        ): Array<Map.Entry<String, Int>> {
            lens.sort()
            val map = LinkedHashMap<String, Int>()
            lens.forEach { len ->
                val beg = floor(len / step) * step
                map["${beg},${beg + step}"] = map.getOrDefault("${beg},${beg + step}", 0) + 1
            }
            return mergeSort(map.entries.toTypedArray(), -1)
        }

        fun colorSummary(imgTensor: NDArray, step: Double): Array<Map.Entry<String, Int>> {
            val dims = imgTensor.shape.shape
            assert(dims.size == 3 && dims[2].toInt() in 1..3 step 2) { "imgTensor shape error" }

            val doubleTensor = imgTensor.toType(DataType.FLOAT64, false) // Convert to double

            val lens = ArrayList<Double>()
            for (idx in 0 until dims[0]) {
                for (idy in 0 until dims[1]) {
                    if (dims[2].toInt() == 1) {
                        lens.add(abs(doubleTensor.getDouble(idx, idy, 0)))
                    } else if (dims[2].toInt() == 3) {
                        lens.add(
                            sqrt(
                                doubleTensor.getDouble(idx, idy, 0).pow(2) + doubleTensor.getDouble(idx, idy, 1)
                                    .pow(2) + doubleTensor.getDouble(idx, idy, 2).pow(2)
                            )
                        )
                    }
                }
            }
            return entries(lens, step)
        }


        fun <T : Number> pairToString(x: T, y: T): String {
            return "${x},${y}"
        }

        fun <T : Number> pairToString(pair: Point<T, T>): String {
            return "${pair.first},${pair.second}"
        }

        fun <T : Number> stringToPoint(str: String): Point<T, T> {
            val parts = str.split(",")
            val x = parts[0].toDouble() as T
            val y = parts[1].toDouble() as T
            return Point(x, y)
        }

        fun tensorToTensor(tensor: NDArray, points: Array<Point<Int, Int>>): NDArray {
            val manager = tensor.manager
            val pointCount = points.size
            val result = manager.create(Shape(1, pointCount.toLong(), 3))
            val middle = FloatArray(pointCount * 3)

            for (i in points.indices) {
                val point = points[i]
                val x = point.first.toLong()
                val y = point.second.toLong()
                val extracted = tensor[x, y].toFloatArray()
                middle[i * 3] = extracted[0]
                middle[i * 3 + 1] = extracted[1]
                middle[i * 3 + 2] = extracted[2]
            }

            result.set(middle)
            return result
        }

        fun imgToTensor(img: BufferedImage, points: Array<Point<Double, Double>>): NDArray {
            val manager = TensorManager.getManager()
            val data = FloatArray(points.size * 3)

            for ((index, point) in points.withIndex()) {
                val x = point.first.toInt()
                val y = point.second.toInt()
                val rgb = img.getRGB(x, y)
                val r = (rgb shr 16 and 0xFF) / 255f
                val g = (rgb shr 8 and 0xFF) / 255f
                val b = (rgb and 0xFF) / 255f
                data[index * 3] = r
                data[index * 3 + 1] = g
                data[index * 3 + 2] = b
            }
            val tensor = manager.create(data, Shape(1, points.size.toLong(), 3))
            return tensor
        }
    }

    fun getOther(): HashSet<String> {
        return otherSet
    }

    fun clearBase() {
        baseSet = null
        dx = 0.0
        dy = 0.0
    }

    fun clearOther() = otherSet.clear()

    fun addBase(pairs: Array<Point<Double, Double>>) {
        var maxX = Double.MIN_VALUE
        var maxY = Double.MIN_VALUE
        var minX = Double.MAX_VALUE
        var minY = Double.MAX_VALUE
        baseSet = pairs
        if (baseSet!!.isEmpty()) return
        baseSet?.forEach { p ->
            if (p.first > maxX) maxX = p.first
            if (p.second > maxY) maxY = p.second
            if (p.first < minX) minX = p.first
            if (p.second < minY) minY = p.second
        }
        dx = 0.1 * (maxX - minX)
        dy = 0.1 * (maxY - minY)
        //        A bit outrageous
    }

    fun clearAddOther(pairs: Array<Point<Double, Double>>) {
        otherSet.clear()
        pairs.mapTo(otherSet) { pairToString(it.first, it.second) }
        quadTree.clearInsert(pairs)
    }

    fun diff(): Double {
        baseSet!!.forEach { p ->
            val queries = quadTree.query(Rectangle(p.first - dx, p.second - dy, 2 * dx, 2 * dy))
            queries.forEach {
                otherSet.remove(pairToString(it.first, it.second))
            }
        }
        val (big, small) = if (baseSet!!.size > otherSet.size) baseSet!!.size to otherSet.size else otherSet.size to baseSet!!.size
        return small / big.toDouble()
    }

    //    normal out api to use
    fun getColorSummary(image: BufferedImage, bound: Array<Point<Int, Int>>): Array<Map.Entry<String, Int>>? {
        return colorTransformer.getColorSummary(image, bound)
    }


    fun colorSummaryCompare(
        base: BufferedImage,
        bound: Array<Point<Int, Int>>,
        other: BufferedImage,
        bound2: Array<Point<Int, Int>>,
    ): Boolean {
        val step = 5.0
        val maxDiff = 0.05
        val baseSummary = colorTransformer.getColorSummary(base, bound)
        val otherSummary = colorTransformer.getColorSummary(other, bound2)
        if (baseSummary == null || otherSummary == null) return false
        val baseSize = baseSummary.size
        val otherSize = baseSummary.size
        if (abs(baseSize - otherSize) / baseSummary.size.toDouble() > maxDiff) return false
        var idx = 0
        while (idx < 10 && idx < otherSize && idx < baseSize) {
            if (baseSummary[idx].key != otherSummary[idx].key) return false
            var df = abs(otherSummary[idx].value - baseSummary[idx].value)
            if (df / baseSummary[idx].value.toDouble() > maxDiff) {
                return false
            }
            idx++
        }
        return true
    }
}
