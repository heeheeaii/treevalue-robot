package com.treevalue.robot.msg

import java.io.Serializable

data class PreviewMsg(val resourceId: String, val preview: ByteArray):Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PreviewMsg

        if (resourceId != other.resourceId) return false
        if (!preview.contentEquals(other.preview)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resourceId.hashCode()
        result = 31 * result + preview.contentHashCode()
        return result
    }
}
