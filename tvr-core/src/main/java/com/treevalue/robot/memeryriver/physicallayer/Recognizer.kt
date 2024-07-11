package com.treevalue.robot.memeryriver.physicallayer

import com.treevalue.robot.anno.TopApi
import com.treevalue.robot.data.MapPair
import com.treevalue.robot.data.Point
import com.treevalue.robot.test.ImageComparator
import java.awt.image.BufferedImage
import kotlin.math.*

class Recognizer {
    val MAX_DIFF = 0.05
    private val maxPointNumber: Int = 300
    private val imageComparator: ImageComparator = ImageComparator()

    @TopApi
    fun visualBoundIsSimilar(
        base: BufferedImage,
        bound: Array<Point<Int, Int>>,
        other: BufferedImage,
        bound2: Array<Point<Int, Int>>,
    ): Boolean {
        return colorRecognize(base, bound, other, bound2) && geometryRecognize(
            pointsToDouble(bound), pointsToDouble(bound2)
        )
    }

    fun <T : Number> pointsToDouble(array: Array<Point<T, T>>): Array<Point<Double, Double>> {
        return array.map { Point(it.first.toDouble(), it.second.toDouble()) }.toTypedArray()
    }

    fun colorRecognize(
        base: BufferedImage,
        bound: Array<Point<Int, Int>>,
        other: BufferedImage,
        bound2: Array<Point<Int, Int>>,
    ): Boolean {
        return imageComparator.colorSummaryCompare(base, bound, other, bound2)
    }

    fun geometryRecognize(points1: Array<Point<Double, Double>>, points2: Array<Point<Double, Double>>): Boolean {
        val (ps1, ps2) = geometryPrepare(points1, points2)
        return compare(ImageComparator(), ps1, ps2)
    }

    private fun geometryPrepare(
        points1: Array<Point<Double, Double>>, points2: Array<Point<Double, Double>>
    ): Array<Array<Point<Double, Double>>> {
        val centers = centralize(points1, points2)
        normalizeWithCenters(centers, points1, points2)
        return extraMaxNumberPoints(points1, points2)
    }

    private fun compare(
        comparator: ImageComparator, points1: Array<Point<Double, Double>>, points2: Array<Point<Double, Double>>
    ): Boolean {
        val map = MapPair(Double.MAX_VALUE, 0.0)
        var internal = PI / 6
        var rad = 0.0
        comparator.addBase(points1)
        comparator.clearAddOther(points2)
        rotateCompare(rad, internal, 2 * PI, comparator, map, points2)
        internal /= 6
        rad = map.second - internal * 5
        rotate(rad, points2)
        comparator.clearAddOther(points2)
        rotateCompare(rad, internal, rad + 11 * internal, comparator, map, points2)
        return map.first < MAX_DIFF
    }

    private fun rotateCompare(
        rad: Double,
        internal: Double,
        end: Double,
        comparator: ImageComparator,
        map: MapPair<Double, Double>,
        points2: Array<Point<Double, Double>>
    ) {
        var rad1 = rad
        while (rad1 < end) {
            val diff = comparator.diff()
            if (diff < map.first) {
                map.first = diff
                map.second = rad1
            }
            rotate(internal, points2)
            comparator.clearAddOther(points2)
            rad1 += internal
        }
    }

    fun <K : Number, T : Number> pointArrayToIntArray(points: Array<Point<K, T>>): Array<Point<Int, Int>> {
        return points.map { Point(it.first.toInt(), it.second.toInt()) }.toTypedArray()
    }

    private fun <K : Number, T : Number> extraMaxNumberPoints(
        vararg pointsList: Array<Point<K, T>>,
    ): Array<Array<Point<K, T>>> {
        val rst = ArrayList<Array<Point<K, T>>>()
        pointsList.map { ps ->
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
                sqrt(it.first.toDouble() * it.first.toDouble() + it.second.toDouble() * it.second.toDouble())
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
                p.first = (p.first - centers[idx].first) / scales[idx]
                p.second = (p.second - centers[idx].second) / scales[idx]
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
                centers[idx].first += it.first.toDouble()
                centers[idx].second += it.second.toDouble()
            }
            centers[idx].first /= (ps.size + 0.0)
            centers[idx].second /= (ps.size + 0.0)
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
                point.first = point.first * cos(radian) - point.second * sin(radian)
                point.second = point.first * sin(radian) + point.second * cos(radian)
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
