package com.treevalue.robot.resource.mongodb.saveless16mresource

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document("tvr_resource_byte")
data class Less16MResourceByte(
    //less than 16m
    @Id
    var id: String? = null,
    val resourceId: String,
    val preview: ByteArray,
    val source: ByteArray?
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Less16MResourceByte) return false

        return resourceId == other.resourceId &&
                preview.contentEquals(other.preview) &&
                source.contentEquals(other.source)
    }

    override fun hashCode(): Int {
        var result = resourceId.hashCode()
        result = 31 * result + preview.contentHashCode()
        result = 31 * result + source.contentHashCode()
        return result
    }
}
