package com.treevalue.robot.modelmachine.recognizer


import com.treevalue.robot.modelmachine.recognizer.visual.VisualMoment
import com.treevalue.robot.modelmachine.recognizer.visual.VisualObjManyMoment
import com.treevalue.robot.modelmachine.recognizer.visual.util.ConversionUtil
import org.apache.tomcat.util.threads.ThreadPoolExecutor
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO


object ImageRecognizer {
    private lateinit var threadPool: ThreadPoolExecutor

    init {
        val corePoolSize = Runtime.getRuntime().availableProcessors()
        val maximumPoolSize = 3 * corePoolSize
        val keepAliveTime: Long = 25000
        val unit = TimeUnit.MILLISECONDS
        val workQueue: BlockingQueue<Runnable> = ArrayBlockingQueue(10 * corePoolSize)
        val threadFactory = Executors.defaultThreadFactory()
        val handler: ThreadPoolExecutor.RejectedExecutionHandler = ThreadPoolExecutor.CallerRunsPolicy()
        threadPool =
            ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
            )
    }

    val lock: Any = Any()

    fun imageRecognize(bufferedImage: BufferedImage): VisualMoment {
        var time: Long
        synchronized(lock) {
            time = System.currentTimeMillis()
        }
        val visualObjManyMoment = VisualObjManyMoment(time)
        visualObjManyMoment.recognizeMoment(bufferedImage)
        val res = VisualMoment(bufferedImage, time, visualObjManyMoment)
        return res
    }

    fun drawObjOutLine(imagePath: String, outputPath: String, color: Int = 0xffffffff.toInt()) {
        try {
            val originalImage: BufferedImage = ImageIO.read(File(imagePath))
//            todo recognize a point and it's bound error possibly is diff judge error
            val visualMoment = imageRecognize(originalImage)

            for (entry in visualMoment.visualObjMoment.featureMap) {
                for (positionLong in entry.value.bound) {
                    val xy = ConversionUtil.longToXY(positionLong)
                    originalImage.setRGB(xy[0], xy[1], color)
                }
            }
            ImageIO.write(originalImage, "png", File(outputPath))
            println("Image processed and saved to $outputPath")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error processing the image.")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val inputPath =
            "D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\resources\\test\\img\\modeldetect\\forest_in_spring.png"
        val outputPath =
            "D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\resources\\modelmachine\\drwaoutlin\\test_javacv.png"
        ImageRecognizer.drawObjOutLine(inputPath, outputPath)
        System.exit(0)
    }
}



