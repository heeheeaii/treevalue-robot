package com.treevalue.robot.modelmachine.recognizer.visual.constant

object VisualConstant {
    val COLORFEATURE_MAX_DIFFERENCE = 0.10
    val COLORFEATUR_MAX_COLOR_LENGTH = 443.403 //sqrt(3*255.999^2)

    val CONVERSION_LONG_LEFT_MOVE_MULTI = 1000000L
    val CONVERSION_FLOAT_TO_LONG_3_DECIMAL_PLACES = 1000
    val CONVERSION_LEFT_MOVE_3_SPACE_MULTI = 1000

    val VISUALOBJ_FEATURE_KEY_STRING_LENGTH = 18

    val TRANSFORM_MAX_DIFF_IN_FEATURE = 0.15
    val TRANSFORM_MAX_DIFF_IN_SPARSE_POINT_NUMBER = 0.1
    val TRANSFORM_MIN_SIMILAR_IN_NOW_PRE_MOMENT = 0.85

    //    c,u,ur, r,dr,d,dl,l,ul for 2d matrix
    val ROTATE_ARRAY = intArrayOf(0, 0, 0, -1, 1, -1, 1, 0, 1, 1, 0, 1, -1, 1, -1, 0, -1, -1)

    val REMEBER_LAKE_REMAINNING_RATE_ALL = 1.0
    val REMEBER_LAKE_SAVE_TO_HARD_TIME_BY_MS = 1000
    val REMEBER_LAKE_POINT_SIZE = 5
}
