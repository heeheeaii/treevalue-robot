package com.treevalue.robot.resource.bigfile.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("tvr_big_resource_preview")
data class BigResourcePreview(
    @Id
    var id: String? = null,
    val resourceId: String,
    val preview: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BigResourcePreview

        if (id != other.id) return false
        if (resourceId != other.resourceId) return false
        if (!preview.contentEquals(other.preview)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + resourceId.hashCode()
        result = 31 * result + preview.contentHashCode()
        return result
    }
}
