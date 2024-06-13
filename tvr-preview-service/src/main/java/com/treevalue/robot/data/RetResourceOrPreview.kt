package com.treevalue.robot.data

import java.io.Serializable

data class RetResourceOrPreview(
    val name: String?,
    val resource: ByteArray?
) : Serializable
