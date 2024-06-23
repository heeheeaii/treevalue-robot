package com.treevalue.robot.data.tree

import com.treevalue.robot.data.Point

// capacity : every layer plain space max include points number
// use in 2-d plain
class QuadTree<T : Number>(
    var boundary: Rectangle,
) {
    private val capacity: Int = 4
    private var points: MutableList<Point<T, T>> = mutableListOf()
    private var children: Array<QuadTree<T>?> = arrayOfNulls(4)

    // clear all points, but keep shape information
    fun clear() {
        points.clear()
        for (idx in children.indices) {
            children[idx] = null
        }
    }

    fun reset(boundary: Rectangle = this.boundary) {
        this.boundary = boundary
        clear()
    }

    fun insert(points: Array<Point<T, T>>) {
        points.forEach { p ->
            insert(p)
        }
    }

    fun clearInsert(points: Array<Point<T, T>>) {
        clear()
        insert(points)
    }

    fun insert(point: Point<T, T>): Boolean {
        if (!boundary.contains(point.first.toDouble(), point.second.toDouble())) {
            return false
        }

        if (points.size < capacity) {
            points.add(point)
            return true
        }

        if (children[0] == null) {
            subdivide()
        }

        var i = 0
        while (i <= 3) {
            if (children[i]!!.insert(point)) {
                return true
            }
            i++
        }

        return false
    }

    private fun subdivide() {
        val xMid = boundary.x + boundary.width / 2
        val yMid = boundary.y + boundary.height / 2

        children[0] = QuadTree(
            Rectangle(boundary.x, boundary.y, xMid - boundary.x, yMid - boundary.y),
        )
        children[1] =
            QuadTree(Rectangle(xMid, boundary.y, boundary.width / 2, yMid - boundary.y))
        children[2] =
            QuadTree(Rectangle(boundary.x, yMid, xMid - boundary.x, boundary.height / 2))
        children[3] =
            QuadTree(Rectangle(xMid, yMid, boundary.width / 2, boundary.height / 2))

        // Re-insert points into the new children
        val oldPoints = points
        points = mutableListOf()
        for (point in oldPoints) {
            for (i in 0..3) {
                if (children[i]!!.insert(point)) {
                    break
                }
            }
        }
    }

    fun query(range: Rectangle): List<Point<T, T>> {
        val foundPoints = mutableListOf<Point<T, T>>()

        if (!boundary.intersects(range)) {
            return foundPoints
        }

        for (point in points) {
            if (range.contains(point.first.toDouble(), point.second.toDouble())) {
                foundPoints.add(point)
            }
        }

        if (children[0] != null) {
            for (child in children) {
                if (child != null) {
                    foundPoints.addAll(child.query(range))
                }
            }
        }

        return foundPoints
    }
}

