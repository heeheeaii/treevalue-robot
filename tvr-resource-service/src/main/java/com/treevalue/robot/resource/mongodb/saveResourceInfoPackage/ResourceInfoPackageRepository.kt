package com.treevalue.robot.resource.mongodb.saveResourceInfoPackage

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface ResourceInfoPackageRepository : MongoRepository<ResourceInfoPackage?, String?> {
    @Query("SELECT rfp FROM  ResourceInfoPackage rfp WHERE rfp.resourceId = :resourceId")
    fun findByResourceId(resourceId: String): ResourceInfoPackage?
}
