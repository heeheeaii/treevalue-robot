package com.treevalue.robot.resource.web.changer

import com.treevalue.robot.resource.mongodb.saveResourceInfoPackage.ResourceInfoPackage
import com.treevalue.robot.resource.remote.FeignApi
import com.treevalue.robot.resource.web.msg.WebResourcePackage
import org.springframework.stereotype.Service

@Service
class MsgToData(private val feignApi: FeignApi) {
    fun changeToResourceInfoPackage(wrp: WebResourcePackage): ResourceInfoPackage {
        val fromUserId = feignApi.parseTokenToUserId(wrp.token).body ?: throw Exception("unauthorized token")
        val fromTime = System.currentTimeMillis()
        return ResourceInfoPackage(
            null, wrp.usefulness, wrp.condition, wrp.previewId, wrp.resourceId, fromUserId, fromTime
        )
    }
}

