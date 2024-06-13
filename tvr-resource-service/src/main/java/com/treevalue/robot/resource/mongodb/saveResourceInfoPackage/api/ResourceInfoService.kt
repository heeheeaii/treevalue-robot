package com.treevalue.robot.resource.mongodb.saveResourceInfoPackage.api

import com.treevalue.robot.resource.mongodb.saveResourceInfoPackage.DeleteResourceInfo
import com.treevalue.robot.resource.mongodb.saveResourceInfoPackage.ResourceDeleteRepository
import com.treevalue.robot.resource.mongodb.saveResourceInfoPackage.ResourceInfoPackage
import com.treevalue.robot.resource.mongodb.saveResourceInfoPackage.ResourceInfoPackageRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
open class ResourceInfoService(
    private val resourceInfoPackageRepository: ResourceInfoPackageRepository,
    private val resourceDeleteRepository: ResourceDeleteRepository,
    ) {

    @Async
    open fun saveResourceInfoPackage(resourceInfoPackage: ResourceInfoPackage?) {
        resourceInfoPackageRepository!!.save(resourceInfoPackage)
    }

    fun findById(resourceId:String): ResourceInfoPackage? {
        val res = resourceInfoPackageRepository!!.findByResourceId(resourceId)
        return res
    }

//    real delete wait kotlin inner schema task to call mongodb function to do
    fun deleteIn(deleteResourceInfo: DeleteResourceInfo){
        resourceDeleteRepository.save(deleteResourceInfo)
    }
}
