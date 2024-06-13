package com.treevalue.robot.modelmachine.recognizer.audio.filter

object AudioPointFilter {
    fun <T : Comparable<T>> smallThan(value: T, threshold: T): Boolean {
        try {
            return value > threshold
        } catch (e: Exception) {
            return false
        }
    }
}
