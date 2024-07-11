package com.treevalue.robot.memeryriver.gradientlayer

import com.treevalue.robot.data.Point

class Rows {
    private val maxSize = 2000
    private var bounds: HashMap<Int, HashSet<Point<Int, Int>>> = HashMap()
    private val rows: LinkedHashMap<Int, ArrayList<Int>> = LinkedHashMap()

    var allNumber: Int = 0
        private set

    private fun push(point: Point<Int, Int>) {
        val row = rows.getOrPut(point.first) { ArrayList() }
        row.add(point.second)
        allNumber++
    }

    fun getInners(mark: Int): Array<Point<Int, Int>> {
        val points = bounds.getOrDefault(mark, hashSetOf())
        points.forEach { push(it) }
        return if (allNumber <= maxSize) {
            points.toTypedArray()
        } else {
            val step = allNumber.toDouble() / maxSize
            var cur = 0.0
            val result = ArrayList<Point<Int, Int>>()
            for ((x, yList) in rows) {
                for (y in yList) {
                    if (cur.toInt().toDouble() == cur) {
                        result.add(Point(x, y))
                    }
                    cur += step
                    if (result.size >= maxSize) break
                }
                if (result.size >= maxSize) break
            }
            result.toTypedArray()
        }
    }

    fun setBounds(bounds: HashMap<Int, HashSet<Point<Int, Int>>>) {
        this.bounds = bounds
    }

    fun curBoundsStatics(): LinkedHashMap<Int, ArrayList<Int>> {
        return rows
    }
}
