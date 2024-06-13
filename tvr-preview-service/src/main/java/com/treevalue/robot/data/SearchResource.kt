package com.treevalue.robot.data

import java.io.Serializable

data class SearchResource(val desc: String, val ext: String, val id: Long, val preview: ByteArray) : Serializable
