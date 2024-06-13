package com.treevalue.robot.resource.jpaes8

import com.treevalue.robot.resource.model.ResourceSearchInfo
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaResourceSearchInfoRepository:ElasticsearchRepository<ResourceSearchInfo,String> {
}
