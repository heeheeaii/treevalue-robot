package com.treevalue.robot.modelmachine.parser.needmorevalication

import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.global.opencv_imgproc
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.MatVector
import org.bytedeco.opencv.opencv_core.Scalar

object TestVideoUtil {
    fun drawContours(imagePath: String, drawOutputPath: String) {
        val image: Mat =
            opencv_imgcodecs.imread(imagePath)
        val grayImage = Mat()
        opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_BGR2GRAY)

        val binaryImage = Mat()
        opencv_imgproc.threshold(
            grayImage,
            binaryImage,
            0.0,
            255.0,
            opencv_imgproc.THRESH_BINARY or opencv_imgproc.THRESH_OTSU
        )

        val contours = MatVector()
        opencv_imgproc.findContours(
            binaryImage,
            contours,
            opencv_imgproc.RETR_EXTERNAL,
            opencv_imgproc.CHAIN_APPROX_SIMPLE
        )

        for (i in 0 until contours.size()) {
            opencv_imgproc.drawContours(image, contours, i.toInt(), Scalar(0.0, 255.0, 0.0, 1.0))
        }
        opencv_imgcodecs.imwrite(
            drawOutputPath,
            image
        )
    }
}
