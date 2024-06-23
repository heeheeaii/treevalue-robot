package com.treevalue.robot.data.tree

import com.treevalue.robot.data.Point


// use in n-d space , by modify K parameter
class KDTree(points: List<Point<Double, Double>>) {
    companion object {
        private const val K = 2 // 2-dimensional space, e.g., x, y, z, t, etc
    }

    private var root: KDNode?

    fun clear() {
        root = null
    }

    fun rebuild(points: List<Point<Double, Double>>, depth: Int) {
        this.root = buildTree(points, depth)
    }

    internal class KDNode(var point: Point<Double, Double>) {
        var left: KDNode? = null
        var right: KDNode? = null
    }

    init {
        this.root = buildTree(points, 0)
    }

    private fun buildTree(points: List<Point<Double, Double>>, depth: Int): KDNode? {
        if (points.isEmpty()) {
            return null
        }
        val axis = depth % K
        val sortedPoints = points.sortedWith(Comparator { a, b ->
            if (axis == 0) {
                a.first.compareTo(b.first)
            } else {
                a.second.compareTo(b.second)
            }
        })
        val medianIndex = sortedPoints.size / 2
        val node = KDNode(sortedPoints[medianIndex])
        node.left = buildTree(sortedPoints.subList(0, medianIndex), depth + 1)
        node.right = buildTree(sortedPoints.subList(medianIndex + 1, sortedPoints.size), depth + 1)
        return node
    }

    fun rangeSearch(lowerLeft: Point<Double, Double>, upperRight: Point<Double, Double>): List<Point<Double, Double>> {
        val result: MutableList<Point<Double, Double>> = ArrayList()
        rangeSearch(root, lowerLeft, upperRight, 0, result)
        return result
    }

    private fun rangeSearch(
        node: KDNode?,
        lowerLeft: Point<Double, Double>,
        upperRight: Point<Double, Double>,
        depth: Int,
        result: MutableList<Point<Double, Double>>
    ) {
        if (node == null) {
            return
        }
        val point = node.point
        if (point.first >= lowerLeft.first && point.first <= upperRight.first && point.second >= lowerLeft.second && point.second <= upperRight.second) {
            result.add(point)
        }
        val axis = depth % K
        if (axis == 0) {
            if (lowerLeft.first <= point.first) {
                rangeSearch(node.left, lowerLeft, upperRight, depth + 1, result)
            }
            if (upperRight.first >= point.first) {
                rangeSearch(node.right, lowerLeft, upperRight, depth + 1, result)
            }
        } else {
            if (lowerLeft.second <= point.second) {
                rangeSearch(node.left, lowerLeft, upperRight, depth + 1, result)
            }
            if (upperRight.second >= point.second) {
                rangeSearch(node.right, lowerLeft, upperRight, depth + 1, result)
            }
        }
    }

}
