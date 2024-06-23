package com.treevalue.robot.actor

import com.treevalue.robot.data.Singleton
import java.awt.event.InputEvent

class Mouse {
    private val robot = Singleton.getRobot()

    fun left() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    }

    fun right() {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
    }

    fun wheel(wheelAmt: Int, mills: Int) {
        robot.mouseWheel(wheelAmt)
        Thread.sleep(mills.toLong())
    }
}
