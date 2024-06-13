package com.treevalue.robot.resource.mongodb.saveless16mresource

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface ResourceByteRepository : MongoRepository<Less16MResourceByte?, String?> {
    @Query("select rsb.preview from Less16MResourceByte rsb where rsb.resourceId = ?1")
    fun findByResourceId(resourceId:String):ByteArray?
}
