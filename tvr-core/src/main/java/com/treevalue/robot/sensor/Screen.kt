package com.treevalue.robot.sensor

import com.treevalue.robot.data.Singleton
import com.treevalue.robot.pool.ThreadPool
import com.treevalue.robot.v3.other.data.Tensor
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.imageio.ImageIO

class Screen {
    private var seeExecuted = AtomicBoolean(false)

    private val robot: Robot = Singleton.getRobot()

    private fun screenShot(): BufferedImage {
        val screenRect = Rectangle(Toolkit.getDefaultToolkit().screenSize)
        return robot.createScreenCapture(screenRect)
    }

    fun screenShotData(): Tensor<UByte> {
        val image = screenShot()
        val dataBuffer = image.raster.dataBuffer as DataBufferByte
        val uByteList = dataBuffer.data.map { it.toUByte() }.toMutableList()
        return Tensor(
            arrayListOf(image.width, image.height), uByteList
        )
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
