package com.treevalue.robot.resource.pgsql.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ResourceFeedbackEvaluationMapper {
    fun findEvaluationByResourceId(@Param("resourceId") resourceId:String):Double?
}
