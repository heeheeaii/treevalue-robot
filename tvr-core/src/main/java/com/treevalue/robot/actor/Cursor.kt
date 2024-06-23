package com.treevalue.robot.actor

import com.treevalue.robot.data.Singleton
import java.awt.AWTException
import java.awt.Robot
import java.awt.event.InputEvent


class Cursor {
    private var robot: Robot? = null

    init {
        try {
            robot = Singleton.getRobot()
        } catch (e: AWTException) {
            e.printStackTrace()
        }
    }

    fun click(x: Int, y: Int) {
        robot!!.mouseMove(x, y)

        robot!!.mousePress(InputEvent.BUTTON1_DOWN_MASK)

        robot!!.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    }
}
