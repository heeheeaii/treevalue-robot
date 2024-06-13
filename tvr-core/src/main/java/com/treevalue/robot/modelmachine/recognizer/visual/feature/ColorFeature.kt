package com.treevalue.robot.modelmachine.recognizer.visual.feature

import com.treevalue.robot.modelmachine.recognizer.visual.constant.VisualConstant
import com.treevalue.robot.modelmachine.recognizer.visual.util.ConversionUtil
import kotlin.math.sqrt

class ColorFeature(
//    default use rgb color, 3 decimal places
    var averageColor: FloatArray = FloatArray(3)
) : Feature {
    companion object {
        val MAX_DIFFERENCE = 0.15
        val MAX_COLOR_LENGTH = 443.403 //sqrt(3*255.999^2)

        fun getFeatureLong(rgbFloatArray: FloatArray): Long {
            return ConversionUtil.rgbToLong3DecimalPlace(rgbFloatArray)
        }


        fun getFeatureString(rgbFloatArray: FloatArray): String {
            return getFeatureString(getFeatureLong(rgbFloatArray))
        }

        fun getFeatureString(featureLong: Long): String {
            val strOrigin = featureLong.toString()
            if (strOrigin.length > VisualConstant.VISUALOBJ_FEATURE_KEY_STRING_LENGTH) {
                throw IllegalArgumentException("decimal spaces of featureLong can't more then ${VisualConstant.VISUALOBJ_FEATURE_KEY_STRING_LENGTH}")
            }
            return if (strOrigin.length < VisualConstant.VISUALOBJ_FEATURE_KEY_STRING_LENGTH) {
                "${"0".repeat(VisualConstant.VISUALOBJ_FEATURE_KEY_STRING_LENGTH - strOrigin.length)}${strOrigin}"
            } else {
                strOrigin
            }

        }

        fun getFeatureLong(rgbIntArray: IntArray): Long {
            val rgbFloatArray = floatArrayOf(
                rgbIntArray[0].toFloat(),
                rgbIntArray[1].toFloat(),
                rgbIntArray[2].toFloat()
            )
            return ConversionUtil.rgbToLong3DecimalPlace(rgbFloatArray)
        }

        fun getLength(rgbIntArray: IntArray): Float {
            checkFormat(rgbIntArray)
            return sqrt(
                (rgbIntArray[0] * rgbIntArray[0] +
                        rgbIntArray[1] * rgbIntArray[1] +
                        rgbIntArray[2] * rgbIntArray[2]).toFloat()
            )
        }

        fun getLength(rgbFloatArray: FloatArray): Float {
            checkFormat(rgbFloatArray)
            return sqrt(
                (rgbFloatArray[0] * rgbFloatArray[0] +
                        rgbFloatArray[1] * rgbFloatArray[1] +
                        rgbFloatArray[2] * rgbFloatArray[2]).toFloat()
            )
        }

        fun getDifferenceLength(rgbArray0: FloatArray, rgbArray1: FloatArray): Float {
            assert(rgbArray0.size == 3 && rgbArray1.size == 3) { "Each RGB array must have exactly 3 elements." }
            val rgbDiff = arrayOf(
                (rgbArray0[0] - rgbArray1[0]),
                (rgbArray0[1] - rgbArray1[1]),
                (rgbArray0[2] - rgbArray1[2])
            )
            return sqrt(
                rgbDiff[0] * rgbDiff[0] +
                        rgbDiff[1] * rgbDiff[1] +
                        rgbDiff[2] * rgbDiff[2]
            )
        }

        fun getDifferenceLength(rgbArray0: IntArray, rgbArray1: IntArray): Float {
            val rgbFloatArray0 = floatArrayOf(
                rgbArray0[0].toFloat(),
                rgbArray0[1].toFloat(),
                rgbArray0[2].toFloat(),
            )
            val rgbFloatArray1 = floatArrayOf(
                rgbArray1[0].toFloat(),
                rgbArray1[1].toFloat(),
                rgbArray1[2].toFloat(),
            )
            return getDifferenceLength(rgbFloatArray0, rgbFloatArray1)
        }

        fun isOneType(differenceLength: Float, rgb1: IntArray, rgb2: IntArray): Boolean {
            val maxLength = getLength(rgb1).coerceAtLeast(getLength(rgb2))
            return if (maxLength == 0f) {
                differenceLength < VisualConstant.COLORFEATUR_MAX_COLOR_LENGTH * VisualConstant.COLORFEATURE_MAX_DIFFERENCE/2
            } else {
                differenceLength < maxLength * VisualConstant.COLORFEATURE_MAX_DIFFERENCE
            }
        }

        fun checkFormat(rgbFloatArray: FloatArray) {
            assert(rgbFloatArray.size == 3)
            assert(rgbFloatArray[0] in 0F..255.999F && rgbFloatArray[1] in 0F..255.999F && rgbFloatArray[2] in 0F..255.999F)
        }

        fun checkFormat(rgbIntArray: IntArray) {
            assert(rgbIntArray.size == 3)
            assert(rgbIntArray[0] in 0..255 && rgbIntArray[1] in 0..255 && rgbIntArray[2] in 0..255)
        }


    }

    fun getFeatureLong(): Long {
        return ConversionUtil.rgbToLong3DecimalPlace(averageColor)
    }

    override fun getFeatureString(): String {
        val featureLong =
            ConversionUtil.rgbToLong3DecimalPlace(averageColor[0], averageColor[1], averageColor[2])
        return ConversionUtil.getStandardString(featureLong)
    }
}
