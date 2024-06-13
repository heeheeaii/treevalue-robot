package com.treevalue.robot.resource.pgsql.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import jakarta.validation.constraints.NotBlank

@TableName("tvr_resource_feedback")
data class ResourceFeedback(
    @NotBlank
    val resourceId: String,
    @NotBlank
    val feedback: String,
    @NotBlank
    val fromUserId: String
) {
    @TableId(value = "id", type = IdType.AUTO)
    var id: String? = null
}
