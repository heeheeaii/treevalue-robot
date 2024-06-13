package com.treevalue.robot.modelmachine.recognizer.visual

import java.awt.image.BufferedImage

data class VisualMoment(
    val image: BufferedImage,
    val time: Long = System.currentTimeMillis(),
    val visualObjMoment: VisualObjManyMoment
) {
}
