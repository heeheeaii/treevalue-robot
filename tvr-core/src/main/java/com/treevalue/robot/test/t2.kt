package com.treevalue.robot.test

data class Point<T, U>(val x: T, val y: U)

fun rotate(vararg pointsList: Array<Point<Double, Double>>, radian: Double) {
    pointsList.forEach { points ->
        points.forEach { point ->
            val newX = point.x * kotlin.math.cos(radian) - point.y * kotlin.math.sin(radian)
            val newY = point.x * kotlin.math.sin(radian) + point.y * kotlin.math.cos(radian)
            println("Old Point: (${point.x}, ${point.y}), New Point: ($newX, $newY)")
        }
    }
}

fun main() {
    val points1 = arrayOf(
        Point(1.0, 0.0),
        Point(0.0, 1.0)
    )

    val points2 = arrayOf(
        Point(-1.0, 0.0),
        Point(0.0, -1.0)
    )

    rotate(points1, points2, radian = kotlin.math.PI / 4) // Rotates points by 45 degrees (π/4 radians)
}
