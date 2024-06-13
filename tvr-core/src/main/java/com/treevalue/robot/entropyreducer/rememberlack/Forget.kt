package com.treevalue.robot.entropyreducer.rememberlack

import com.treevalue.robot.entropyreducer.rememberlack.constance.RememberConstance
import com.treevalue.robot.entropyreducer.rememberlack.io.DataFileUtils
import java.io.File
import java.time.Duration

object Forget {
    private val step = RememberConstance.FORGET_STEP_RATE_ONE_DAY
    val intervalTime = Duration.ofMillis((step * 24 * 60 * 60 * 1000).toLong())

    //  from one step to 48 step coordinating remaining rate
    val memoryRemainingRate = arrayOf(
        0.9698,
        0.9421,
        0.9027,
        0.8531,
        0.7962,
        0.7355,
        0.6746,
        0.6164,
        0.5627,
        0.5146,
        0.4727,
        0.4368,
        0.4065,
        0.3812,
        0.3604,
        0.3433,
        0.3294,
        0.3181,
        0.3090,
        0.3017,
        0.2958,
        0.2911,
        0.2873,
        0.2843,
        0.2819,
        0.2800,
        0.2785,
        0.2773,
        0.2763,
        0.2755,
        0.2749,
        0.2744,
        0.2740,
        0.2737,
        0.2735,
        0.2733,
        0.2731,
        0.2730,
        0.2729,
        0.2728,
        0.2728,
        0.2727,
        0.2727,
        0.2727,
        0.2726,
        0.2726,
        0.2726,
        0.2726,
    )

    fun ebbinghausForgettingCurve(time: Double): Double {
        // µ1 Acquired intensity (during learning) of
        // hippocampal/MTL proc
        // learning intensity
        val u1 = 3.5
        // µ2 Consolidation rate to the neocortical process
        // remember rate
        val u2 = 0.1
        // a1 Decline rate of hippocampal/MTL proc
        // forget intensity
        val a1 = 1.1
        val p = 1 - Math.exp(-u1 * Math.exp(-1 * (a1 * time)) - (u1 * u2) / a1 * (1 - Math.exp(-1 * a1 * time)))
        return p
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val file =
            File("D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\data.txt")
        val arrayList = MutableList<Double>(48) { idx ->
            Forget.ebbinghausForgettingCurve(idx * step)
        }
        DataFileUtils.printArray(arrayList.toTypedArray(), 4)
    }
}
