package com.treevalue.robot.ext

import java.awt.image.BufferedImage
import java.io.*

fun BufferedImage.getRgb(x: Int, y: Int): IntArray {
    val rgb = getRGB(x, y)
    val red = (rgb shr 16) and 0xFF
    val green = (rgb shr 8) and 0xFF
    val blue = rgb and 0xFF
    return intArrayOf(red, green, blue)
}

fun BufferedImage.arrayToRgb(rgb: IntArray): Int {
    require(rgb.size == 3) { "RGB array must have 3 elements" }
    val red = rgb[0].coerceIn(0..255)
    val green = rgb[1].coerceIn(0..255)
    val blue = rgb[2].coerceIn(0..255)
    return (red shl 16) or (green shl 8) or blue
}

fun Serializable.deepClone(): Any? {
    val bos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(bos)
    oos.writeObject(this)
    val bis = ByteArrayInputStream(bos.toByteArray())
    val ois = ObjectInputStream(bis)
    return ois.readObject()
}
