package com.treevalue.robot.test

import com.treevalue.robot.memeryriver.physicallayer.RawImageClassify
import java.io.File
import javax.imageio.ImageIO


object QueueExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val img =
            ImageIO.read(File("D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\lyy.jpg"))
        val ric = RawImageClassify(img)
//        val cons = ric.connects
//        ImageIO.write(ric.createBufferedDyeingImageFromMatrix(),"png",File("D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\lyy-2.jpg"))
    }
}
