package com.treevalue.robot.entropyreducer.logiclake

import com.treevalue.robot.entropyreducer.logiclake.model.DynamicRealModel
import com.treevalue.robot.entropyreducer.logiclake.model.LogicModel
import com.treevalue.robot.entropyreducer.logiclake.model.StaticRealModel
import com.treevalue.robot.entropyreducer.rememberlack.RememberFrame
import com.treevalue.robot.entropyreducer.rememberlack.constance.RememberConstance
import com.treevalue.robot.entropyreducer.rememberlack.data.CircularQueue

class LogicLack {
    private val logicRememberLack =
        CircularQueue<RememberFrame>(RememberConstance.REMEMBER_LAKE_SHORT_REMEMBER_LAKE_SIZE)

    fun extractStaticRealModel(frame: RememberFrame, rememberLake: CircularQueue<RememberFrame>): StaticRealModel {
        val res = StaticRealModel()
        return res

    }

    fun extractDynamicRealModel(frame: RememberFrame, rememberLake: CircularQueue<RememberFrame>): DynamicRealModel {
        val res = DynamicRealModel()
        return res

    }

    fun putModel() {}
    fun findModel(): LogicModel? {
        return null
    }
}
