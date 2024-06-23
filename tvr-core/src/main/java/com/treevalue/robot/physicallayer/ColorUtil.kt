package com.treevalue.robot.physicallayer

import kotlin.math.abs

object ColorUtil {
    fun rgbToInt(rgb: IntArray): Int {
        require(rgb.size == 3) { "rgb array length must be 3." }
        return rgb[0] shl 16 or (rgb[1] shl 8) or rgb[2]
    }

    fun diff(color1: IntArray, color2: IntArray): Double {
        require(color1.size == 3 && color2.size == 3)
        color1.forEach { require(it in 0..255) }
        color2.forEach { require(it in 0..255) }
        return abs(color1[0] - color2[0]) / color1[0].coerceAtLeast(color2[0]).toDouble()
        +abs(color1[1] - color2[1]) / color1[1].coerceAtLeast(color2[1]).toDouble()
        +abs(color1[2] - color2[2]) / color1[2].coerceAtLeast(color2[2]).toDouble()
    }

    fun intToRgb(rgb: Int): IntArray {
        return intArrayOf(
            rgb shr 16 and 0xFF,
            rgb shr 8 and 0xFF,
            rgb and 0xFF
        )
    }
}

