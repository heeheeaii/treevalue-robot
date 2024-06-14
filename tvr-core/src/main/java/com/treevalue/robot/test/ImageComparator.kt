package com.treevalue.robot.test

import com.treevalue.robot.data.Point
import kotlin.math.sqrt

class ImageComparator {
    private val baseSet = HashSet<Long>()
    private val otherSet = HashSet<Long>()

    fun getOther(): HashSet<Long> {
        return otherSet
    }
    fun clearBase() = baseSet.clear()

    fun clearOther() = otherSet.clear()

    fun addBase(pairs: Array<Point<Int, Int>>) {
        pairs.mapTo(baseSet) { pairToLong(it) }
    }

    fun addOther(pairs: Array<Point<Int, Int>>) {
        pairs.mapTo(otherSet) { pairToLong(it) }
    }

    fun diff(): Double {
        val (big, small) = if (baseSet.size > otherSet.size) baseSet to otherSet else otherSet to baseSet
        return (big - small).size / big.size.toDouble()
    }

    companion object {
        // Cantor pairing function
        fun pairToLong(pair: Point<Int, Int>): Long {
            val (x, y) = pair
            return (((x + y) * (x + y + 1) / 2) + y).toLong()
        }

        // Cantor pairing function inverse
        fun longToPoint(z: Long): Point<Int, Int> {
            val w = ((sqrt(8.0 * z + 1) - 1) / 2).toLong()
            val t = (w * w + w) / 2
            val y = z - t
            val x = w - y
            return Point(x.toInt(), y.toInt())
        }
    }
}
