package com.treevalue.robot.resource.pgsql.model

import com.baomidou.mybatisplus.annotation.TableName
import jakarta.validation.constraints.NotBlank

@TableName("tvr_resource_feedback_evaluation")
data class ResourceFeedbackEvaluation(
    @NotBlank
    val resourceId: String,
    @NotBlank
    val feedbackLevel: Double
)
