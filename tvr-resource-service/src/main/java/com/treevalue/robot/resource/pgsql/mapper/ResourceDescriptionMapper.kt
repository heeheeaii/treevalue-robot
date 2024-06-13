package com.treevalue.robot.resource.pgsql.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.treevalue.robot.resource.pgsql.config.MybatisRedisCache
import com.treevalue.robot.resource.pgsql.model.ResourceDescription
import com.treevalue.robot.resource.pgsql.msg.ResourcePriceAndEvaluationMsg
import org.apache.ibatis.annotations.CacheNamespace
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.springframework.cache.annotation.Cacheable

@Mapper
@CacheNamespace(implementation = MybatisRedisCache::class)
interface ResourceDescriptionMapper : BaseMapper<ResourceDescription> {

    @Cacheable(cacheNames = ["resource"], key = "#resourceId")
    fun findByResourceID(@Param("resourceId") resourceId: String): ResourceDescription?


    fun findPriceAndEvaluationByResourceId(@Param("resourceId") resourceId: String):ResourcePriceAndEvaluationMsg?
}
