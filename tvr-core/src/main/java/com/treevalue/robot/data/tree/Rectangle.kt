package com.treevalue.robot.data.tree

import com.treevalue.robot.data.Point

data class Rectangle(var x: Double, var y: Double, var width: Double, var height: Double) {

    fun update(x: Double = this.x, y: Double = this.y, width: Double = this.width, height: Double = this.height) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
    }


    fun contains(x: Double, y: Double): Boolean {
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height
    }

    fun <T : Number> contains(point: Point<T, T>): Boolean {
        return point.first.toDouble() >= x && point.first.toDouble() <= x + width && point.second.toDouble() >= y && point.second.toDouble() <= y + height
    }

    fun intersects(other: Rectangle): Boolean {
        return !(other.x + other.width < x ||
                other.x > x + width ||
                other.y + other.height < y ||
                other.y > y + height)
    }
}
