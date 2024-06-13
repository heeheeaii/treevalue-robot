package com.treevalue.robot.resource.mongodb.saveResourceInfoPackage

import org.springframework.data.mongodb.repository.MongoRepository

interface ResourceDeleteRepository: MongoRepository<DeleteResourceInfo?, String?> {
}
