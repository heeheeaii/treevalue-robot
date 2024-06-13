package com.treevalue.robot.resource.bigfile.repository

import com.treevalue.robot.resource.bigfile.data.BigResourcePreview
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface BigResourcePreviewRepository : MongoRepository<BigResourcePreview?, String?> {
    @Query("SELECT preview FROM tvr_big_resource_preview brp WHERE brp.resourceId = :resourceId")
    fun findByResourceId(resourceId: String): ByteArray?

}
