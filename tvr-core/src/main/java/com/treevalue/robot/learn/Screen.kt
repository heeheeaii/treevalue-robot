package com.treevalue.robot.learn

import com.treevalue.robot.data.Singleton
import com.treevalue.robot.pool.ThreadPool
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.imageio.ImageIO


class Screen {
    private var seeExecuted = AtomicBoolean(false)

    private val robot: Robot = Singleton.getRobot()

    fun screenShot(): BufferedImage {
        val screenRect = Rectangle(Toolkit.getDefaultToolkit().screenSize)
        return robot.createScreenCapture(screenRect)
    }

    fun screenShot(path: String) {
        val image = screenShot()
        ImageIO.write(image, "png", File(path))
    }

    fun see() {
        if (!seeExecuted.get()) {
            seeExecuted.set(true)
            val cache = Singleton.getVisualCachePool()
            val task = Runnable {
                val screenRect = Rectangle(Toolkit.getDefaultToolkit().screenSize)
                val image = Singleton.getRobot().createScreenCapture(screenRect)
                cache.push(image)
            }
            ThreadPool.getScheduler().scheduleAtFixedRate(task, 0, 48, TimeUnit.MILLISECONDS)
        }
    }
}
