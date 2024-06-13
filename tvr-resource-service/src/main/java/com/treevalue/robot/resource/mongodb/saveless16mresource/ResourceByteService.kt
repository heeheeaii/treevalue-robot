package com.treevalue.robot.resource.mongodb.saveless16mresource

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
open class ResourceByteService(private val resourceByteRepository: ResourceByteRepository) {

    @Async
    open fun saveResource(less16MResourceByte: Less16MResourceByte?) {
        resourceByteRepository!!.save(less16MResourceByte)
    }

}
