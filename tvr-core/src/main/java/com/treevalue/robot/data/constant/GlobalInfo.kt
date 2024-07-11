package com.treevalue.robot.data.constant

import java.awt.Toolkit

object GlobalInfo {
    val visualWidth: Int
    val visualHeight: Int

    init {
        val size = Toolkit.getDefaultToolkit().screenSize
        visualWidth = size.width
        visualHeight = size.height
    }
}
