package com.treevalue.robot.resource.mongodb.util

import com.mongodb.client.gridfs.model.GridFSFile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream

@Service
class GridFsUtil(val gridFsTemplate: GridFsTemplate) {

    fun store(file: MultipartFile): String? {
        val objectId: String?
        try {
            objectId = gridFsTemplate.store(file.inputStream, file.originalFilename, file.contentType).toString()
        } catch (e: IOException) {
            throw RuntimeException("Failed to store file", e)
        }
        return objectId
    }

    fun getFile(id: String): GridFSFile? {
        val res = gridFsTemplate.findOne(Query.query(Criteria.where("_id").`is`(id)))
        return res
    }

    fun getInputStream(gridFsFile: GridFSFile): InputStream {
        return gridFsTemplate.getResource(gridFsFile).inputStream
    }


}
