package com.treevalue.robot.modelmachine.recognizer.visual.util

import com.treevalue.robot.modelmachine.recognizer.visual.constant.VisualConstant
import java.math.BigInteger


object ConversionUtil {
    //    inner color string format 18 bit decimal spaces, red 6 green 6 blue 6
//    NOTE : outer data must change to inner format to deal
    private const val LONG_LEFT_MOVE_MULTI = 1000000L
    private const val FLOAT_TO_LONG_3_DECIMAL_PLACES = 1000
    private const val LEFT_MOVE_3_SPACE_MULTI = 1000

    fun rgbToLong3DecimalPlace(rgbIntArray: IntArray): Long {
        assert(rgbIntArray.size == 3)
        assert(rgbIntArray[0] in 0..255 && rgbIntArray[1] in 0..255 && rgbIntArray[2] in 0..255)
        return rgbToLong3DecimalPlace(
            rgbIntArray[0].toFloat(),
            rgbIntArray[1].toFloat(),
            rgbIntArray[2].toFloat()
        )
    }


    fun longToIntRgb(compressedNumber: Long): IntArray {
//        todo :has exception
        val rgbFloatArray = longToFloatRgb3DecimalPlaces(compressedNumber)
        val res = intArrayOf(
            rgbFloatArray[0].toInt(),
            rgbFloatArray[1].toInt(),
            rgbFloatArray[2].toInt()
        )
        return res
    }

    fun rgbToLong3DecimalPlace(rgbFloatArray: FloatArray): Long {
        assert(rgbFloatArray.size == 3)
        assert(rgbFloatArray[0] in 0.0..255.999 && rgbFloatArray[1] in 0.0..255.999 && rgbFloatArray[2] in 0.0..255.999)
        return rgbToLong3DecimalPlace(rgbFloatArray[0], rgbFloatArray[1], rgbFloatArray[2])
    }

    fun rgbToLong3DecimalPlace(red: Float, green: Float, blue: Float): Long {
        var tmp = (red * FLOAT_TO_LONG_3_DECIMAL_PLACES).toLong()
        var redBigInt = BigInteger.valueOf(tmp)
        redBigInt = redBigInt.multiply(BigInteger.valueOf(LONG_LEFT_MOVE_MULTI * LONG_LEFT_MOVE_MULTI))
        tmp = (green * FLOAT_TO_LONG_3_DECIMAL_PLACES).toLong()
        var greenBigInt = BigInteger.valueOf(tmp)
        greenBigInt = greenBigInt.multiply(BigInteger.valueOf(LONG_LEFT_MOVE_MULTI))
        tmp = (blue * FLOAT_TO_LONG_3_DECIMAL_PLACES).toLong()
        var blueBigInt = BigInteger.valueOf(tmp)
        return redBigInt.toLong() + greenBigInt.toLong() + blueBigInt.toLong()
    }

    fun longToFloatRgb3DecimalPlaces(compressedNumber: String): FloatArray {
        return longToFloatRgb3DecimalPlaces(compressedNumber.toLong())
    }



    fun getStandardString(featureLong: Long): String {
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

    fun longToFloatRgb3DecimalPlaces(compressedNumber: Long): FloatArray {
        val str = getStandardString(compressedNumber)
        val red = str.substring(0, 6).toFloat() / FLOAT_TO_LONG_3_DECIMAL_PLACES
        val green = str.substring(6, 12).toFloat() / FLOAT_TO_LONG_3_DECIMAL_PLACES
        val blue = str.substring(12, 18).toFloat() / FLOAT_TO_LONG_3_DECIMAL_PLACES
        return floatArrayOf(red, green, blue)
    }

    fun XYTOLong(x: Int, y: Int): Long {
        return x.toLong() shl 32 or (y and 0xffffffffL.toInt()).toLong()
    }

    inline fun longToXY(num: Long): IntArray {
        // [x, y]
        val xy = IntArray(2)
        xy[0] = (num shr 32).toInt()
        xy[1] = (num and 0xffffffffL).toInt()
        return xy
    }


    fun intToRgbIntArray(number: Int): IntArray {
//        red green blue
//        only can use in parse normal rgb wrapper number for rgb extra as r g b individual
        val rgb = IntArray(3)
        rgb[0] = number shr 16 and 0xff
        rgb[1] = number shr 8 and 0xff
        rgb[2] = number and 0xff
        return rgb
    }
}

