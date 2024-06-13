package com.treevalue.robot.modelmachine.recognizer.visual

import com.treevalue.robot.global.GlobalData
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.ConcurrentSkipListSet

class VisualMomentObjPool {
    private val momentTimeQueue: ConcurrentSkipListSet<Long> = ConcurrentSkipListSet()
    private val waitCount: ConcurrentHashMap<Long, Int> = ConcurrentHashMap()
    private val momentPool: ConcurrentSkipListMap<Long, VisualObjManyMoment> = ConcurrentSkipListMap()
    fun pop(): VisualObjManyMoment? {
        val key = momentPool.firstEntry()?.key ?: return null
        if (key == momentTimeQueue.first) {
            momentTimeQueue.pollFirst()
            if (waitCount.contains(key)) {
                waitCount.remove(key)
            }
            return momentPool.pollFirstEntry().value
        }
        if (!waitCount.contains(key)) {
            waitCount[key] = 1
        } else {
            waitCount[key] = waitCount[key]!! + 1
            if (waitCount[key]!! > GlobalData.VISUALMOMENTPOOL_MAX_WAIT) {
                waitCount.remove(key)
                momentTimeQueue.remove(key)
            }
        }
        return null
    }

    fun putMomentTime(time: Long) {
        momentTimeQueue.add(time)
    }

    fun putVisualMoment(timeByMill: Long, visualObjManyMoment: VisualObjManyMoment) {
        if (timeByMill != visualObjManyMoment.time) {
            throw IllegalArgumentException("time must equal visualObjManyMoment's itme")
        }
        if (momentPool.contains(timeByMill)) {
            throw IllegalArgumentException("The real word feeling timestamp must unique in time river")
        }
        momentTimeQueue.add(timeByMill)
        momentPool[timeByMill] = visualObjManyMoment
    }
}
