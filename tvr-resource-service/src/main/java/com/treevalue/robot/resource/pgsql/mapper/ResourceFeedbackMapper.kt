package com.treevalue.robot.resource.pgsql.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.treevalue.robot.resource.pgsql.model.ResourceFeedback
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ResourceFeedbackMapper : BaseMapper<ResourceFeedback> {
    fun findByResourceID(@Param("resourceId") resourceId: String):  ResourceFeedback
    fun findFeedbackLevelByResourceId(@Param("resourceId") resourceId: String): Double
}
