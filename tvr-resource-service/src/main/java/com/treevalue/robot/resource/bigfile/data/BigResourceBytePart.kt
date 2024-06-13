package com.treevalue.robot.resource.bigfile.data

import org.springframework.data.mongodb.core.mapping.Document


@Document("tvr_big_resource_part")
data class BigResourceBytePart(
    val id: String?,
    val resourceSequence: Int,
    val resource: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BigResourceBytePart

        if (id != other.id) return false
        if (resourceSequence != other.resourceSequence) return false
        if (!resource.contentEquals(other.resource)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + resourceSequence
        result = 31 * result + resource.contentHashCode()
        return result
    }
}
