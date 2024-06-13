package com.treevalue.robot.entropyreducer.rememberlack

import com.treevalue.robot.entropyreducer.rememberlack.constance.RememberConstance
import com.treevalue.robot.entropyreducer.rememberlack.data.CircularQueue
import org.apache.tomcat.util.threads.ThreadPoolExecutor
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit


class ShortRememberLake {
    private var previewForgetTime: Long = System.currentTimeMillis()
    private val shortRememberLake =
        CircularQueue<RememberFrame>(RememberConstance.REMEMBER_LAKE_SHORT_REMEMBER_LAKE_SIZE)
    private val taskPool: ThreadPoolExecutor

    init {
        val coresNumber = Runtime.getRuntime().availableProcessors()
        taskPool = ThreadPoolExecutor(
            coresNumber,
            coresNumber * 4,
            10,
            TimeUnit.MINUTES,
            LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            ThreadPoolExecutor.AbortPolicy()
        )
    }

    fun continueRemember(frame: RememberFrame?) {
        if (frame == null) return
        shortRememberLake.addFirst(frame!!)
    }

    suspend fun forgetPoints() {
        val curMillis = System.currentTimeMillis()
        if (curMillis - previewForgetTime < RememberConstance.FORGET_INTERVAL_TIME) return
        for (idx in Forget.memoryRemainingRate.indices) {
            var beg =
                (RememberConstance.FORGET_STEP_RATE_ONE_DAY * idx * RememberConstance.FORGET_FRAME_NUMBER_ONE_DAY).toInt()
            val end =
                (RememberConstance.FORGET_STEP_RATE_ONE_DAY * (idx + 1) * RememberConstance.FORGET_FRAME_NUMBER_ONE_DAY).toInt()
            if (idx == 0) beg = RememberConstance.REMEBER_LAKE_REAL_ZONE_SIZE
            forget(beg, end, Forget.memoryRemainingRate[idx])
        }
    }

    private fun forget(beg: Int, end: Int, remainingRate: Double) {
        for (idx in beg until end) {
            forget(shortRememberLake.get(beg), remainingRate)
        }
    }

    private inline fun forget(frame: RememberFrame?, remainingRate: Double) {
        frame?.forgetPoint(remainingRate)
    }

    fun forgetModel() {

    }

}
