package com.treevalue.robot.pointSet

import com.treevalue.robot.anno.Fixed

object PointSetUtil {
    val XU = 0
    val YU = 1
    val XLU = -1
    val YLU = 1
    val XD = 0
    val YD = -1
    val XRU = 1
    val YRU = 1
    val XLD = -1
    val YLD = -1
    val XRD = 1
    val YRD = -1
    val XL = -1
    val YL = 0
    val XR = 1
    val YR = 0

    @Fixed
//    from right then counterclockwise a round
    fun getMove(): Array<Int> {
        return arrayOf(XR, YR, XRU, YRU, XU, YU, XLU, YLU, XL, YL, XRD, YRD, XD, YD, XRD, YRD)
    }
}
