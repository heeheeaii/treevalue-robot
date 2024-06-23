package com.treevalue.robot.test.pointSet

import com.treevalue.robot.data.Point
import java.awt.Color
import java.awt.Graphics
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.math.cos
import kotlin.math.sin

class PointUtil {
    private var points: MutableList<Point<Double, Double>> = mutableListOf()
    private var originalPoints: List<Point<Double, Double>> = listOf()

    fun savePointsToFile(filename: String) {
        File(filename).bufferedWriter().use { out ->
            points.forEach { point ->
                out.write("${point.first}, ${point.second}\n")
            }
        }
    }

    fun getPointsAsArray(): Array<Point<Double, Double>> {
        return points.toTypedArray()
    }

    fun load(pointList: MutableList<Point<Double, Double>>) {
        points = pointList
    }

    fun load(filename: String) {
        points.clear()
        File(filename).forEachLine { line ->
            val (x, y) = line.split(", ").map { it.toDouble() }
            points.add(Point(x, y))
        }
        originalPoints = points.toList()
    }

    // 缩放点集
    fun zoom(scaleFactor: Double) {
        points = points.map { Point(it.first * scaleFactor, it.second * scaleFactor) }.toMutableList()
    }

    // 旋转点集
    fun rotate(degrees: Double) {
        val radians = Math.toRadians(degrees)
        val cosAngle = cos(radians)
        val sinAngle = sin(radians)
        points = points.map {
            Point(
                it.first * cosAngle - it.second * sinAngle, it.first * sinAngle + it.second * cosAngle
            )
        }.toMutableList()
    }

    // 获取点集
    fun getPoints(): List<Point<Double, Double>> = points
    fun getOriginalPoints(): List<Point<Double, Double>> = originalPoints

    // 获取点集的中心
    fun getCenter(points: List<Point<Double, Double>>): Point<Double, Double> {
        val centerX = points.map { it.first }.average()
        val centerY = points.map { it.second }.average()
        return Point(centerX, centerY)
    }

    fun drawPoints(
        originalPoints: List<Point<Double, Double>>,
        points: List<Point<Double, Double>>,
        width: Int = 2,
        height: Int = 2
    ) {
        val panel = PointPanel(originalPoints, points)
        val frame = JFrame()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(width, height)
        frame.add(panel)
        frame.isVisible = true
    }

    companion object {
        class PointPanel(
            private val originalPoints: List<Point<Double, Double>>,
            private val points: List<Point<Double, Double>>
        ) : JPanel() {

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                val originalCenter = getCenter(originalPoints)
                val transformedCenter = getCenter(points)

                val xOffset = width / 2
                val yOffset = height / 2

                g.color = Color.RED
                for (point in originalPoints) {
                    g.fillOval(
                        (point.first - originalCenter.first).toInt() + xOffset,
                        (point.second - originalCenter.second).toInt() + yOffset,
                        5,
                        5
                    )
                }

                g.color = Color.BLUE
                for (point in points) {
                    g.fillOval(
                        (point.first - transformedCenter.first).toInt() + xOffset,
                        (point.second - transformedCenter.second).toInt() + yOffset,
                        5,
                        5
                    )
                }
            }

            private fun getCenter(points: List<Point<Double, Double>>): Point<Double, Double> {
                val centerX = points.map { it.first }.average()
                val centerY = points.map { it.second }.average()
                return Point(centerX, centerY)
            }
        }
    }
}
