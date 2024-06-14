package com.treevalue.robot.test.ui

import javax.swing.JFrame

class UiUtil {
    fun getFrame(width: Int = 500, height: Int = 800, name: String = "frame"): JFrame {
        val frame = JFrame(name)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(width, height)
        return frame
    }

    fun display(frame: JFrame) {
        frame.isVisible = true
    }

}
