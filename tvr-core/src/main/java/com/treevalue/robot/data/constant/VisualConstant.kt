package com.treevalue.robot.data.constant

import java.util.concurrent.atomic.AtomicBoolean

object VisualConstant {
    private val visualLock: AtomicBoolean = AtomicBoolean(false)

    var visualMatrixWidthHeight: IntArray? = null
        set(value) {
            if (value == null) return
            if (!visualLock.get()) {
                synchronized(visualLock) {
                    if (!visualLock.get()) {
                        require(value.size == 2) { "visual matrix size don't right" }
                        field = value
                        visualLock.set(true)
                    }
                }
            }
        }
}
