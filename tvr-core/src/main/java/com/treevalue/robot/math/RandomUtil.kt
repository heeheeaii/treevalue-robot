package com.treevalue.robot.math

import kotlin.random.Random

object RandomUtil {
    fun geneIndexByProbability(probabilities: DoubleArray): Int {
        val total = probabilities.sum()
        val normalizedProbabilities = probabilities.map { it / total }

        val randomValue = Random.nextDouble()

        var cumulativeProbability = 0.0
        for ((index, prob) in normalizedProbabilities.withIndex()) {
            cumulativeProbability += prob
            if (randomValue <= cumulativeProbability) {
                return index
            }
        }

        return -1
    }
}
