package com.treevalue.robot.physicallayer

import ai.djl.ndarray.NDArray
import com.treevalue.robot.data.MapPair
import com.treevalue.robot.data.Point
import com.treevalue.robot.test.ImageComparator
import kotlin.math.*

class Recognizer {
    val MAX_DIFF = 0.05
    private val maxPointNumber: Int = 300

    fun forward(tensor: Array<NDArray>): Array<NDArray> {
        TODO()
    }

    fun forward(points1: Array<Point<Double, Double>>, points2: Array<Point<Double, Double>>): Boolean {
        prepare(points1, points2)
        return compare(ImageComparator(), points1, points2)
    }

    private fun prepare(
        points1: Array<Point<Double, Double>>, points2: Array<Point<Double, Double>>
    ) {
        val centers = centralize(points1, points2)
        normalizeWithCenters(centers, points1, points2)
        extraMaxNumberPoints(points1, points2)
    }

    private fun compare(
        comparator: ImageComparator, points1: Array<Point<Double, Double>>, points2: Array<Point<Double, Double>>
    ): Boolean {
        val map = MapPair<Double, Double>(Double.MAX_VALUE, 0.0)
        var internal = PI / 6
        var rad = 0.0
        comparator.addBase(pointArrayToIntArray(points1))
        comparator.addOther(pointArrayToIntArray(points2))
        rotateCompare(rad, internal, comparator, map, points2)
        internal /= 6
        rad = map.second - internal * 5
        rotateCompare(rad, internal, comparator, map, points2)
        return map.first < MAX_DIFF
    }

    private fun rotateCompare(
        rad: Double,
        internal: Double,
        comparator: ImageComparator,
        map: MapPair<Double, Double>,
        points2: Array<Point<Double, Double>>
    ) {
        var rad1 = rad
        while (rad1 < rad1 + internal * 11) {
            val diff = comparator.diff()
            if (diff < map.first) {
                map.first = diff
                map.second = rad1
            }
            rotate(internal, points2)
            comparator.addOther(pointArrayToIntArray(points2))
            rad1 += internal
        }
    }

    fun <K : Number, T : Number> pointArrayToIntArray(points: Array<Point<K, T>>): Array<Point<Int, Int>> {
        return points.map { Point(it.x.toInt(), it.y.toInt()) }.toTypedArray()
    }

    private fun <K : Number, T : Number> extraMaxNumberPoints(
        vararg pointsList: Array<Point<K, T>>,
    ): Array<Array<Point<K, T>>> {
        val rst = ArrayList<Array<Point<K, T>>>()
        val map = pointsList.map { ps ->
            if (ps.size > maxPointNumber) {
                rst.add(extractBySize(maxPointNumber, ps).toTypedArray())
            } else {
                rst.add(ps)
            }
        }
        return rst.toTypedArray()
    }

    fun <K : Number, T : Number> scale(vararg pointsList: Array<Point<K, T>>): List<Double> {
        return pointsList.map { ps ->
            ps.map {
                sqrt(it.x.toDouble() * it.x.toDouble() + it.y.toDouble() * it.y.toDouble())
            }.average()
        }
    }

    private fun normalizeWithCenters(
        centers: Array<Point<Double, Double>>, vararg pointsList: Array<Point<Double, Double>>
    ) {
        val scales = scale(*pointsList)
        for (idx in 0 until pointsList.size) {
            val ps = pointsList[idx]
            ps.forEach { p ->
                p.x = (p.x - centers[idx].x) / scales[idx]
                p.y -= (p.y - centers[idx].y) / scales[idx]
            }
        }
    }

    private fun normalize(vararg pointsList: Array<Point<Double, Double>>) {
        val centers = centralize(*pointsList)
        normalizeWithCenters(centers, *pointsList)
    }

    private fun <K : Number, T : Number> centralize(vararg pointsList: Array<Point<K, T>>): Array<Point<Double, Double>> {
        val centers = Array(pointsList.size) { Point(0.0, 0.0) }
        for (idx in 0 until pointsList.size) {
            val ps = pointsList[idx]
            ps.forEach {
                centers[idx].x += it.x.toDouble()
                centers[idx].y += it.y.toDouble()
            }
            centers[idx].x /= (ps.size + 0.0)
            centers[idx].y /= (ps.size + 0.0)
        }
        return centers
    }

    fun <T> extractBySize(size: Int, array: Array<T>): ArrayList<T> {
        assert(size in 1..array.size) { "extract size scale should in 1 until ${array.size}" }
        val m = array.size
        val n = size
        val step = (m.toDouble() - n) / (n - 1)
        val r = ArrayList<T>(n)
        for (i in 0 until n) {
            val idx = floor(i + step * i)
            r.add(array[idx.toInt()])
        }
        return r
    }

    /**
    Counterclockwise
    clockwise:
    point.x = point.x * kotlin.math.cos(radian) + point.y * kotlin.math.sin(radian)
    point.y = point.y * kotlin.math.cos(radian) - point.x * kotlin.math.sin(radian)
     */
    fun rotate(radian: Double, vararg pointsList: Array<Point<Double, Double>>) {
        pointsList.forEach { points ->
            points.forEach { point ->
                point.x = point.x * cos(radian) - point.y * sin(radian)
                point.y = point.x * sin(radian) + point.y * cos(radian)
            }
        }
    }

    fun degreesToRadians(degrees: Double): Double {
        return degrees * PI / 180.0
    }

    fun radiansToDegrees(radians: Double): Double {
        return radians * 180.0 / PI
    }
}
