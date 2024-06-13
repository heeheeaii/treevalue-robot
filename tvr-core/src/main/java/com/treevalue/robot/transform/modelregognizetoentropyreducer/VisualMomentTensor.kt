package com.treevalue.robot.transform.modelregognizetoentropyreducer

import com.treevalue.robot.entropyreducer.rememberlack.RememberFrame
import com.treevalue.robot.entropyreducer.rememberlack.ShortRememberUtil
import com.treevalue.robot.modelmachine.recognizer.visual.VisualMoment
import com.treevalue.robot.modelmachine.recognizer.visual.VisualObjOne
import com.treevalue.robot.modelmachine.recognizer.visual.constant.VisualConstant
import com.treevalue.robot.modelmachine.recognizer.visual.util.ConversionUtil
import org.bytedeco.javacpp.indexer.FloatRawIndexer
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat
import org.bytedeco.opencv.global.opencv_core
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.opencv_core.Mat
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import java.awt.image.BufferedImage


class VisualMomentTensor : MomentModel {
    var time: Long = 0
    lateinit var visualTensor: INDArray
    lateinit var markSparseInnerPoints: HashMap<String, Array<Long>>

    constructor() {
        throw Exception("can't call no parameter constructor")
    }

    constructor(visualMoment: VisualMoment) {
        time = visualMoment.time
        visualTensor = visualMomentToTensor(visualMoment)
        markSparseInnerPoints = getSparseInnerPoints(visualMoment.visualObjMoment.featureMap)
    }

    companion object {
        val BOUND_INDEX = 3
        val MARK_INDEX = 4
        val IS_BOUND = 1F
        private fun getSparseInnerPoints(markMap: HashMap<String, VisualObjOne>): HashMap<String, Array<Long>> {
            val res = HashMap<String, Array<Long>>()
            markMap.forEach {
                res[it.key] = it.value.sparseInnerPoints.toTypedArray()
            }
            return res
        }

        fun visualMomentToTensor(visualMoment: VisualMoment): INDArray {
//        [red, green, blue, bound, feature]
//        if bound is isBound , then feature will mark feature string
            val imageMat = imageToMat(visualMoment.image)
            if (imageMat.empty()) {
//            default float
                return Nd4j.empty()
            }
            val width = imageMat.cols()
            val height = imageMat.rows()
            val bound = 1
            val mark = 1
            val channels = imageMat.channels()
            val pointsSize = bound + channels + mark
            val floatImage = Mat()
            imageMat.convertTo(floatImage, opencv_core.CV_32F)
            val indexer = imageMat.createIndexer<FloatRawIndexer>()
            val tensorData = FloatArray(width * height * pointsSize)
            indexer.use {
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        for (c in 0 until channels) {
                            tensorData[y * width * pointsSize + x * pointsSize + c] =
                                indexer[y.toLong(), x.toLong(), c.toLong()]
                        }
                    }
                }
            }

            visualMoment.visualObjMoment.markMap.forEach {
                val visualObj = it.value
                val mark = it.key.toFloat()
                visualObj.bound.forEach {
                    val positionLong = it
                    val xy = ConversionUtil.longToXY(positionLong)
                    tensorData[xy[1] * width * pointsSize + xy[0] * pointsSize + BOUND_INDEX] = IS_BOUND
                    tensorData[xy[1] * width * pointsSize + xy[0] * pointsSize + MARK_INDEX] = mark
                }
            }

            val tensor = Nd4j.create(tensorData, intArrayOf(1, height, width, pointsSize))
            return tensor
        }

        private inline fun imageToMat(bufferedImage: BufferedImage): Mat {
            val converter = Java2DFrameConverter()
            val frame: Frame = converter.convert(bufferedImage)
            val converterToMat = ToMat()
            return converterToMat.convert(frame)
        }

        fun imageToTensor(bufferedImage: BufferedImage): INDArray {
            return imageToTensor(imageToMat(bufferedImage))
        }

        fun imageToTensor(imagePath: String) {
            val imageData = opencv_imgcodecs.imread(imagePath, opencv_imgcodecs.IMREAD_COLOR)
            imageToTensor(imageData)
        }

        fun imageToTensor(imageData: Mat): INDArray {
            if (imageData.empty()) {
//            default float
                return Nd4j.empty()
            }
            val width = imageData.cols()
            val height = imageData.rows()
            val channels = imageData.channels()
            val floatImage = Mat()
            imageData.convertTo(floatImage, opencv_core.CV_32F)
            val indexer = imageData.createIndexer<FloatRawIndexer>()
            val tensorData = FloatArray(width * height * channels)
            indexer.use {
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        for (c in 0 until channels) {
                            tensorData[y * width * channels + x * channels + c] =
                                indexer[y.toLong(), x.toLong(), c.toLong()]
                        }
                    }
                }
            }
            val tensor = Nd4j.create(tensorData, intArrayOf(1, height, width, channels))
            return tensor
        }

        fun remarkVisualMoment(now: VisualMomentTensor, preview: VisualMomentTensor) {
            var reverseSimilarSet: HashSet<String>
            var forwardSimilarSet: HashSet<String>
            val towMomentMatch: HashMap<String, String>
            reverseSimilarSet = fromToIsSimilar(now, preview)
            forwardSimilarSet = fromToIsSimilar(preview, now)
//            tow dir one obj -> map<String, String>
            towMomentMatch = matchMomentVisualObject(
                reverseSimilarSet,
                now,
                forwardSimilarSet,
                preview
            )
            ShortRememberUtil.ShortRememberLake.continueRemember(now.toRememberFrame())
        }

        private fun matchMomentVisualObject(
            nowSimilarSet: HashSet<String>,
            now: VisualMomentTensor,
            previewSimilarSet: HashSet<String>,
            preview: VisualMomentTensor
        ): HashMap<String, String> {
            val res = HashMap<String, String>()
            val isSimilar = HashMap<String, Int>()
            nowSimilarSet.forEach {
                val mark = it
                val points = now.markSparseInnerPoints[mark]!!
                points.forEach {
                    var similar = true
                    for (idx in VisualConstant.ROTATE_ARRAY.indices) {
                        val xy = ConversionUtil.longToXY(it)
//                        todo get item in x y
                        val markPre = getPreMark(preview.visualTensor, xy[0], xy[1], MARK_INDEX)
                        if (previewSimilarSet.contains(markPre)) {
                            isSimilar[markPre] = (isSimilar[markPre] ?: 0) + 1
                            if (isSimilar[markPre]!! / points.size >= VisualConstant.TRANSFORM_MIN_SIMILAR_IN_NOW_PRE_MOMENT) {
                                res[mark] = markPre
                            }
                        }

                    }
                    isSimilar.clear()
                }
            }
            return res
        }

        private inline fun getPreMark(tensor: INDArray, vararg idx: Int): String {
            if (idx.size > tensor.rank()) return ""
            return tensor.getFloat(*idx).toLong().toString()
        }


        private fun fromToIsSimilar(
            now: VisualMomentTensor,
            preview: VisualMomentTensor,
        ): HashSet<String> {
            var sparseSimilarNumber = 0
            val similarSet = HashSet<String>()
            now.markSparseInnerPoints.forEach {
                val markEntry = it
                markEntry.value.forEach {
                    val xy = ConversionUtil.longToXY(it)
                    if (isOneFeature(xy, now.visualTensor, preview.visualTensor)) {
                        sparseSimilarNumber++
                    }
                }
                if (sparseSimilarNumber / (markEntry.value.size + 0.0) < VisualConstant.TRANSFORM_MAX_DIFF_IN_SPARSE_POINT_NUMBER) {
                    similarSet.add(markEntry.key)
                }
            }
            return similarSet
        }

        private fun isOneFeature(xy: IntArray, visualTensor: INDArray, visualTensor1: INDArray): Boolean {
            TODO("Not yet implemented")
        }


        fun TensorToParseData() {

        }

        fun ParseDataToTensor() {}


    }

    fun toRememberFrame(): RememberFrame? {
        return RememberFrame(time, this, VisualConstant.REMEBER_LAKE_REMAINNING_RATE_ALL)
    }

}
