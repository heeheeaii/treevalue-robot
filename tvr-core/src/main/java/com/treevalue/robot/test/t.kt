package com.treevalue.robot.test

import com.treevalue.robot.physicallayer.RawImageClassify
import java.io.File
import javax.imageio.ImageIO


object QueueExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val img =
            ImageIO.read(File("D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\lyy.jpg"))
        RawImageClassify(img)
    }
}
