package com.treevalue.robot.resource.bigfile.repository

import com.treevalue.robot.resource.bigfile.data.BigResourceBytePart
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface BigResourceByteRepository : MongoRepository<BigResourceBytePart?, String?> {
    @Query("SELECT resource FROM tvr_big_resource_part brp WHERE brp.resourceId = :resourceId")
    fun findByResourceId(resourceId: String): List<BigResourceBytePart?>

}
