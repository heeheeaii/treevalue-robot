package com.treevalue.robot.resource.bigfile.api

import com.treevalue.robot.resource.bigfile.data.BigResourceBytePart
import com.treevalue.robot.resource.bigfile.data.BigResourcePreview
import com.treevalue.robot.resource.bigfile.repository.BigResourceByteRepository
import com.treevalue.robot.resource.bigfile.repository.BigResourcePreviewRepository
import org.springframework.stereotype.Service


@Service
open class BigResourceService(
    private val bigResourceByteRepository: BigResourceByteRepository,
    private val bigResourcePreviewRepository: BigResourcePreviewRepository
) {
    fun getSource(resourceId: String): List<BigResourceBytePart?> {
        val res = bigResourceByteRepository.findByResourceId(resourceId)
        return res
    }

    fun getPreview(resourceId: String): ByteArray? {
        val res = bigResourcePreviewRepository.findByResourceId(resourceId)
        return res
    }

    fun savePreview(bigResourcePreview: BigResourcePreview): Boolean {
        val res = bigResourcePreviewRepository.save(bigResourcePreview) != null
        return res
    }


    fun delete(resourceId: String) {
        bigResourcePreviewRepository.deleteById(resourceId)
        bigResourceByteRepository.deleteById(resourceId)
    }

}
