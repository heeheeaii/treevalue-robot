package com.treevalue.robot.test.pointSet

import java.awt.Color
import java.awt.Graphics
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.math.cos
import kotlin.math.sin

class PointUtil {
    private var points: MutableList<Point> = mutableListOf()
    private var originalPoints: List<Point> = listOf()

    fun savePointsToFile(filename: String) {
        File(filename).bufferedWriter().use { out ->
            points.forEach { point ->
                out.write("${point.x}, ${point.y}\n")
            }
        }
    }

    fun load(pointList: MutableList<Point>) {
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
        points = points.map { Point(it.x * scaleFactor, it.y * scaleFactor) }.toMutableList()
    }

    // 旋转点集
    fun rotate(degrees: Double) {
        val radians = Math.toRadians(degrees)
        val cosAngle = cos(radians)
        val sinAngle = sin(radians)
        points = points.map {
            Point(
                it.x * cosAngle - it.y * sinAngle, it.x * sinAngle + it.y * cosAngle
            )
        }.toMutableList()
    }

    // 获取点集
    fun getPoints(): List<Point> = points
    fun getOriginalPoints(): List<Point> = originalPoints

    // 获取点集的中心
    fun getCenter(points: List<Point>): Point {
        val centerX = points.map { it.x }.average()
        val centerY = points.map { it.y }.average()
        return Point(centerX, centerY)
    }

    companion object {
        class PointPanel(private val originalPoints: List<Point>, private val points: List<Point>) : JPanel() {

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                val originalCenter = getCenter(originalPoints)
                val transformedCenter = getCenter(points)

                val xOffset = width / 2
                val yOffset = height / 2

                g.color = Color.RED
                for (point in originalPoints) {
                    g.fillOval(
                        (point.x - originalCenter.x).toInt() + xOffset,
                        (point.y - originalCenter.y).toInt() + yOffset,
                        5,
                        5
                    )
                }

                g.color = Color.BLUE
                for (point in points) {
                    g.fillOval(
                        (point.x - transformedCenter.x).toInt() + xOffset,
                        (point.y - transformedCenter.y).toInt() + yOffset,
                        5,
                        5
                    )
                }
            }

            private fun getCenter(points: List<Point>): Point {
                val centerX = points.map { it.x }.average()
                val centerY = points.map { it.y }.average()
                return Point(centerX, centerY)
            }
        }

        data class Point(val x: Double, val y: Double)

        @JvmStatic
        fun main(args: Array<String>) {
            val pointUtil = PointUtil()

            // 加载点
            pointUtil.load("D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\points.txt")
            println("Original Points: ${pointUtil.getOriginalPoints()}")

            // 缩放点集
            pointUtil.zoom(2.0)
            println("Zoomed Points: ${pointUtil.getPoints()}")

            // 旋转点集
            pointUtil.rotate(90.0)
            pointUtil.savePointsToFile("D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\points-2.txt")
            println("Rotated Points: ${pointUtil.getPoints()}")

            // 创建绘图窗口
            val frame = JFrame("Point Set")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.setSize(800, 800)
            frame.add(PointPanel(pointUtil.getOriginalPoints(), pointUtil.getPoints()))
            frame.isVisible = true
        }

    }
}
