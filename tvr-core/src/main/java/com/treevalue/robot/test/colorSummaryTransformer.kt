package com.treevalue.robot.test

import com.treevalue.robot.data.Point
import com.treevalue.robot.ext.deepClone
import com.treevalue.robot.ext.getRgb
import com.treevalue.robot.pointSet.PointSetUtil
import java.awt.image.BufferedImage
import java.io.Serializable

class ColorSummaryTransformer {
    private var pointMatrixLocal: Array<Array<PointData>>? = null
    private var imageLocal: BufferedImage? = null
    private var maxX = Int.MIN_VALUE
    private var maxY = Int.MIN_VALUE
    private var minX = Int.MAX_VALUE
    private var minY = Int.MAX_VALUE

    class PointData(
        var bound: Boolean = false,
        var outer: Boolean = false,
    ) : Serializable

    class PointHash(val x: Int, val y: Int) : Serializable {
        var then: Array<PointHash>? = null
        var visited: Boolean = false
        override fun hashCode(): Int {
            return x * 1000 + y
        }
    }

    fun updateImage(image: BufferedImage) {
        imageLocal = image
    }

    fun updateBound(bound: Array<Point<Int, Int>>): Array<Array<PointData>>? {
        return initBackground(seal(bound))
    }

    fun getColorSummary(image: BufferedImage, bound: Array<Point<Int, Int>>): Array<Map.Entry<String, Int>>? {
        val sealBound = seal(bound)
        val matrix = initBackground(sealBound)
        val innerColor = extractColors()
        return if (innerColor.isNotEmpty()) {
            ImageComparator.colorSummary(innerColor, 5.0)
        } else null
    }

    private fun initBackground(sealBound: Array<Array<Point<Int, Int>>>): Array<Array<PointData>>? {
        resetPoints()
        if (sealBound.isEmpty()) {
            pointMatrixLocal = null
            return null
        }
        sealBound.forEach { bound ->
            bound.forEach { p ->
                maxX = if (p.first > maxX) p.first else maxX
                maxY = if (p.second > maxY) p.second else maxY
                minX = if (p.first < minX) p.first else minX
                minY = if (p.second < minY) p.second else minY
            }
        }
        val set = HashSet<Int>()
        sealBound.forEach {
            pointsToSet(it, set)
        }
        val matrix = Array(maxX - minX + 1) { Array(maxY - minY + 1) { PointData() } }
        sealBound.forEach { bound ->
            bound.forEach { p ->
                matrix[p.first - minX][p.second - minY].bound = true
            }
        }
        pointMatrixLocal = matrix
        var preBound = false
        var can = true
        for (idx in matrix.indices) {
            can = true
            preBound = false
            for (jdx in matrix[0].indices) {
                if (set.contains(pointKey(idx + minX, jdx + minY))) {
                    if (can) can = false
                    else if (!can && !preBound) can = true
                    preBound = true
                } else {
                    if (can) matrix[idx][jdx].outer = true
                    preBound = false
                }
            }
        }
        return matrix
    }

    fun pointsToSet(points: Array<Point<Int, Int>>, set: HashSet<Int> = HashSet()): HashSet<Int> {
        points.forEach {
            set.add(pointKey(it.first, it.second))
        }
        return set
    }

    fun renewPoints(points: Array<Point<Int, Int>>) {
        val sealPoints = seal(points)
        initBackground(sealPoints)
    }

    fun pointKey(p: Point<Int, Int>): Int {
        return p.first * 1000 + p.second
    }

    fun pointKey(x: Int, y: Int): Int {
        return x * 1000 + y
    }

    fun getHashPoints(points: Array<Point<Int, Int>>): HashMap<Int, PointHash> {
        val rst = HashMap<Int, PointHash>()
        points.forEach {
            rst[pointKey(it)] = PointHash(it.first, it.second)
        }
        val move = PointSetUtil.getMove()
        val list = HashSet<PointHash>()
        rst.entries.forEach {
            val x = it.key / 1000
            val y = it.key % 1000
            for (idx in 0 until move.size / 2) {
                val key = pointKey(move[2 * idx] + x, move[2 * idx + 1] + y)
                if (rst.containsKey(key)) list.add(rst[key]!!)
            }
            if (list.isNotEmpty()) {
                it.value.then = list.toTypedArray()
            }
            list.clear()
        }
        return rst
    }

    fun seal(points: Array<Point<Int, Int>>): Array<Array<Point<Int, Int>>> {
        val map = getHashPoints(points)
        val mid = HashSet<Int>()
        val rst = ArrayList<Array<Point<Int, Int>>>()
        map.entries.first()?.value?.visited = true
        val entries = map.values.toTypedArray().deepClone() as Array<PointHash>
        entries.forEach {
            findSeal(it, mid, it, rst, map)
        }
        return rst.toTypedArray().map { it }.toTypedArray()
    }

    private fun findSeal(
        head: PointHash,
        mid: HashSet<Int>,
        point: PointHash,
        rst: ArrayList<Array<Point<Int, Int>>>,
        allPoint: HashMap<Int, PointHash>
    ) {
        point.visited = true
        val key = pointKey(point.x, point.y)
        mid.add(key)
        if (head == point && mid.size > 1) {
            val tmp = ArrayList<Point<Int, Int>>()
            mid.forEach {
                val hashPoint = allPoint[it]
                if (hashPoint != null) {
                    allPoint.remove(it)
                    tmp.add(Point(hashPoint!!.x, hashPoint.y))
                }
            }
            if (tmp.isNotEmpty()) rst.add(tmp.toTypedArray())
            mid.clear()
            tmp.clear()
            head.then?.forEach { itm ->
                if (!itm.visited) {
                    val key = pointKey(head.x, head.y)
                    allPoint[key] = head
                    mid.add(key)
                    findSeal(head, mid, itm, rst, allPoint)
                }
            }
            return
        }
        if (point.then == null) {
            mid.forEach {
                allPoint.remove(it)
            }
            mid.clear()
            head.then?.forEach { itm ->
                if (!itm.visited) {
                    allPoint[key] = head
                    mid.add(pointKey(head.x, head.y))
                    findSeal(head, mid, itm, rst, allPoint)
                }
            }
            return
        }
        point.then!!.forEach { itm ->
            if (!itm.visited) {
                findSeal(head, mid, itm, rst, allPoint)
            }
        }
    }

    fun isInner(point: Point<Int, Int>): Boolean {
        return try {
            pointMatrixLocal!![point.first - minX][point.second - minY].outer
        } catch (e: Exception) {
            false
        }
    }

    fun extractColors(): List<Triple<Int, Int, Int>> {
        val matrix = pointMatrixLocal
        val img = imageLocal
        val rst = ArrayList<Triple<Int, Int, Int>>()
        if (matrix == null || img == null) return rst
        for (idx in matrix.indices) {
            for (jdx in matrix[0].indices) {
                if (!matrix[idx][jdx].outer) {
                    val rgb = img.getRgb(idx + minX, jdx + minY)
                    rst.add(Triple(rgb[0], rgb[1], rgb[2]))
                }
            }
        }
        return rst
    }

    fun getAInner(): Point<Int, Int>? {
        val matrix = pointMatrixLocal ?: return null
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                if (!matrix[x - minX][y - minY].outer) {
                    return Point(x, y)
                }
            }
        }
        return null
    }

    fun resetPoints() {
        pointMatrixLocal = null
        maxX = Int.MIN_VALUE
        maxY = Int.MIN_VALUE
        minX = Int.MAX_VALUE
        minY = Int.MAX_VALUE
    }

    fun resetAll() {
        resetPoints()
        imageLocal = null
    }
}
