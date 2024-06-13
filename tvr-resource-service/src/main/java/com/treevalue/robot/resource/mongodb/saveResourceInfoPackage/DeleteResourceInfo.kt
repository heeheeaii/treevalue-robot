package com.treevalue.robot.resource.mongodb.saveResourceInfoPackage

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigInteger
import java.util.Date

@Document("tvr_delete_resource_delay_info")
data class DeleteResourceInfo(
    @Id
    val id: BigInteger?,
    val willRealDeleteTime: Date,
    val resourceId:String
)
