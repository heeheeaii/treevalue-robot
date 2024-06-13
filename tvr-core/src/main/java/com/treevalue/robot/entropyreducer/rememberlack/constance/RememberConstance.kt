package com.treevalue.robot.entropyreducer.rememberlack.constance

import java.time.Duration

object RememberConstance {

    // remaining data scale  and set to deal different scale hard storage environment
    private val scaling = 0.1

    val FORGET_INTERVAL_TIME = Duration.ofHours(5).toMillis()

    val FORGET_STEP_RATE_ONE_DAY = FORGET_INTERVAL_TIME.toDouble() / Duration.ofDays(1).toMillis()

    var FORGET_FRAME_NUMBER_ONE_DAY: Int = 2073600

    //    10day , 1 seconds 24 remember frame
    var REMEMBER_LAKE_SHORT_REMEMBER_LAKE_SIZE: Int = 20736000

    //    to 10 sec
    var REMEBER_LAKE_REAL_ZONE_SIZE: Int = 240

    //    to 3 day
    var REMOMBER_LAKE_FAMILIAR_ZONE: Int = 6220800

    //    to 10day
    var REMOMBER_LAKE_FORGET_ZONE: Int = REMEMBER_LAKE_SHORT_REMEMBER_LAKE_SIZE

    init {
        REMEMBER_LAKE_SHORT_REMEMBER_LAKE_SIZE = (scaling * REMEMBER_LAKE_SHORT_REMEMBER_LAKE_SIZE).toInt()
        REMEBER_LAKE_REAL_ZONE_SIZE = (scaling * REMEBER_LAKE_REAL_ZONE_SIZE).toInt()
        REMOMBER_LAKE_FAMILIAR_ZONE = (scaling * REMOMBER_LAKE_FAMILIAR_ZONE).toInt()
        REMOMBER_LAKE_FORGET_ZONE = (scaling * REMOMBER_LAKE_FORGET_ZONE).toInt()
        FORGET_FRAME_NUMBER_ONE_DAY = (scaling * FORGET_FRAME_NUMBER_ONE_DAY).toInt()
    }
}
