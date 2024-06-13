package com.treevalue.robot.modelmachine.recognizer.visual

import com.treevalue.robot.modelmachine.recognizer.visual.feature.ColorFeature
import com.treevalue.robot.modelmachine.recognizer.visual.util.ConversionUtil
import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class VisualObjManyMoment(
    val time: Long = Long.MIN_VALUE,
) {
    val markMap: HashMap<String, VisualObjOne> = HashMap()
    val featureMap: HashMap<String, VisualObjOne> = HashMap()

    val logger = LoggerFactory.getLogger(VisualObjManyMoment::class.java)

    //todo test ke-tree to improve program
    fun recognizeMoment(img: BufferedImage) {
        val width = img.width
        val height = img.height
        val momentMark: AtomicLong = AtomicLong(0)
        val allFeatureSet = HashSet<String>()
        val imgMap = Array(img.width) { Array(img.height) { IntArray(3) } }

//        todo many time 1  can't continue do
        for (idx in 0 until width) {
            for (jdx in 0 until height) {
                val rgbIntArray = ConversionUtil.intToRgbIntArray(img.getRGB(idx, jdx))
//                logger.info("info:r ${rgbIntArray[0]} g:${rgbIntArray[1]} b:${2}")
                imgMap[idx][jdx] = rgbIntArray
            }
        }

        for (idx in 0 until width) {
            for (jdx in 0 until height) {
                val featureString = recognizeFeature(imgMap[idx][jdx], allFeatureSet)
                addColorFeatureToAll(
                    featureString, imgMap[idx][jdx], idx, jdx, allFeatureSet, imgMap, momentMark
                )
            }
        }
    }

    private fun addColorFeatureToAll(
        featureString: String,
        rgbIntArray: IntArray,
        x: Int,
        y: Int,
        allFeatureSet: HashSet<String>,
        imgMap: Array<Array<IntArray>>,
        momentMark: AtomicLong
    ) {
        val positionLong = ConversionUtil.XYTOLong(x, y)
        if (allFeatureSet.contains(featureString)) {
//            todo kd-tree to improve
            val visualObjOne = featureMap[featureString]
            val visualObj = visualObjOne!!
            visualObj.allRGBCounter[0] += rgbIntArray[0]
            visualObj.allRGBCounter[1] += rgbIntArray[1]
            visualObj.allRGBCounter[2] += rgbIntArray[2]
            val allPointNumber = visualObj.allPointNumber.incrementAndGet()
            val averageFloatArray = floatArrayOf(
                (visualObj.allRGBCounter[0] / allPointNumber).toFloat(),
                (visualObj.allRGBCounter[1] / allPointNumber).toFloat(),
                (visualObj.allRGBCounter[2] / allPointNumber).toFloat()
            )
            val averageFeatureString = ColorFeature.getFeatureString(averageFloatArray)
            visualObj.averageFeature = averageFeatureString
            if (isBound(x, y, imgMap)) {
                visualObj.bound.add(positionLong)
            } else {
                if (visualObj.canPutToSparsePoints()) {
                    visualObj.sparseInnerPoints.add(positionLong)
                }
            }
            allFeatureSet.remove(featureString)
            allFeatureSet.add(visualObj.averageFeature)
            featureMap.remove(featureString)
            featureMap[visualObj.averageFeature]=visualObj
        } else {
            allFeatureSet.add(featureString)
            val bounds = mutableListOf(positionLong)
            val sparseInnerPoints = mutableListOf(positionLong)
            val visualObjOne = VisualObjOne(
                momentMark.getAndIncrement().toString(),
                featureString,
                bounds,
                sparseInnerPoints
            )
            markMap[visualObjOne.mark] = visualObjOne
            featureMap[featureString] = visualObjOne
        }
    }

    private fun isBound(x: Int, y: Int, imgMap: Array<Array<IntArray>>): Boolean {
        if (x == 0 || x == imgMap.size - 1 || y == 0 || y == imgMap[0].size - 1) return true
        val rgbIntArray = imgMap[x][y]
        for (idx in -1..1) {
            for (jdx in -1..1) {
                if (idx == 0 && jdx == 0) continue
                val newX = x + idx
                val newY = y + jdx
                val rgbAroundIntArray = imgMap[newX][newY]
                val differenceLength = ColorFeature.getDifferenceLength(rgbIntArray, rgbAroundIntArray)
                if (!ColorFeature.isOneType(differenceLength, rgbIntArray, rgbAroundIntArray)) {
                    return true
                }
            }
        }
        return false
    }

    private fun recognizeFeature(rgbIntArray: IntArray, allFeatureSet: HashSet<String>): String {
        val curFeatureLong = ColorFeature.getFeatureLong(rgbIntArray)
        val curFeatureLength = ColorFeature.getLength(rgbIntArray)
        val similarOrderMap = TreeMap<String, Long>()
        val rgbFloatArray = floatArrayOf(
            rgbIntArray[0].toFloat(), rgbIntArray[1].toFloat(), rgbIntArray[2].toFloat()
        )
        for (featureString in allFeatureSet) {
            val rgbFeatureFloatArray = ConversionUtil.longToFloatRgb3DecimalPlaces(featureString)
            val differenceLength =
                ColorFeature.getDifferenceLength(rgbFloatArray, rgbFeatureFloatArray)
            val featureLength = ColorFeature.getLength(rgbFeatureFloatArray)
            if (differencesAcceptable(differenceLength, curFeatureLength, featureLength)) {
                similarOrderMap[featureString] = curFeatureLong
            }
        }
        if (similarOrderMap.size > 0) {
            return similarOrderMap.firstEntry().key
        }
        return ColorFeature.getFeatureString(curFeatureLong)
    }

    private fun differencesAcceptable(
        differenceLength: Float, curFeatureLength: Float, featureLength: Float
    ): Boolean {
        val maxLength = Math.max(curFeatureLength, featureLength)
        return if (maxLength == 0F) {
            return differenceLength < ColorFeature.MAX_COLOR_LENGTH * ColorFeature.MAX_DIFFERENCE
        } else {
            differenceLength / maxLength <= ColorFeature.MAX_DIFFERENCE
        }
    }
}
