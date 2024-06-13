package com.treevalue.robot.djl.transfer

import ai.djl.ModelException
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.ImageFactory
import ai.djl.ndarray.NDArray
import ai.djl.ndarray.NDManager
import ai.djl.translate.TranslateException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Visual : AutoCloseable {
    private val ndManager: NDManager

    init {
        ndManager = NDManager.newBaseManager()
    }

    @Throws(IOException::class, ModelException::class, TranslateException::class)
    fun imgToTensor(imgPath: String): NDArray {
        val path = Paths.get(imgPath)
        return imgToTensor(path)
    }


    @Throws(IOException::class, ModelException::class, TranslateException::class)
    fun imgToTensor(imgPath: Path): NDArray {
        val image = ImageFactory.getInstance().fromFile(imgPath)
        return image.toNDArray(ndManager)
    }

    @Throws(IOException::class, ModelException::class, TranslateException::class)
    fun tensorToImg(imgTensor: NDArray, outputPath: String, type: String = "jpg"): Image {
        return tensorToImg(imgTensor, Paths.get(outputPath), type)
    }

    @Throws(IOException::class, ModelException::class, TranslateException::class)
    fun tensorToImg(imgTensor: NDArray, outputPath: Path, type: String = "jpg"): Image {
        val outputImage = ImageFactory.getInstance().fromNDArray(imgTensor)
        outputImage.save(Files.newOutputStream(outputPath), type)
        return outputImage
    }

    override fun close() {
        ndManager.close()
    }
}
