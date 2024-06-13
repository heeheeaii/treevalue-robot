package com.treevalue.robot.resource.mongodb.saveResourceInfoPackage

import jakarta.validation.constraints.NotBlank
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigInteger

@Document("tvr_resource_info_package")
data class ResourceInfoPackage(
    @Id
    val id: BigInteger?,
    @NotBlank
    val usefulness: String,
    val condition: List<String>,
    @NotBlank
    val previewId: String,
    val resourceId: String?,
    @NotBlank
    val fromId: String,
    @NotBlank
    val fromTime: Long
)
