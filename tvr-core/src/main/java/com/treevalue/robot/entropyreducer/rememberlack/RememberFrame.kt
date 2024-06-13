package com.treevalue.robot.entropyreducer.rememberlack

import com.treevalue.robot.transform.modelregognizetoentropyreducer.MomentModel

data class RememberFrame(
    val time: Long,
    val momentModels: MomentModel,
    var nowRemainingRate: Double
) {
    fun forgetPoint(remainingRate: Double) {
        if (remainingRate >= remainingRate) return
    }
}
