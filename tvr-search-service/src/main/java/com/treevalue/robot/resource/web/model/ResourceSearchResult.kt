package com.treevalue.robot.resource.web.model

import java.io.Serializable

data class ResourceSearchResult(
    val resourceId: String,
    val preview: ByteArray,
    val conditions: List<String>,
    val usefulness: String,
    val feedbackLevel: Double,
    val price: Double,
    val evaluation: Double
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResourceSearchResult

        if (resourceId != other.resourceId) return false
        if (!preview.contentEquals(other.preview)) return false
        if (usefulness != other.usefulness) return false
        if (conditions != other.conditions) return false
        if (feedbackLevel != other.feedbackLevel) return false
        if (price != other.price) return false
        if (evaluation != other.evaluation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resourceId.hashCode()
        result = 31 * result + preview.contentHashCode()
        result = 31 * result + usefulness.hashCode()
        result = 31 * result + conditions.hashCode()
        result = 31 * result + feedbackLevel.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + evaluation.hashCode()
        return result
    }
}
