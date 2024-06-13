package com.treevalue.robot.resource.web.msg

import jakarta.validation.constraints.NotBlank
import java.io.Serializable

data class WebResourcePackage(
    @NotBlank val usefulness: String,
    val condition: List<String>,
    @NotBlank val previewId: String,
    val resourceId: String?,
    @NotBlank val token: String,
) : Serializable
