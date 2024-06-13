package com.treevalue.robot.resource.mongodb.util
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.bson.types.Binary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MongodbUtil @Autowired constructor(mongoClient: MongoClient) {
    private var DATABASE: MongoDatabase? = null
    private var COLLECTION: MongoCollection<Document>? = null

    init {
        DATABASE = mongoClient.getDatabase("mg")
        COLLECTION = DATABASE?.getCollection("mg")
    }

    fun storeBytes(bytes: ByteArray): String {
        val document = Document("bytes", Binary(bytes))
        COLLECTION?.insertOne(document)
        return document.getObjectId("_id")?.toString() ?: ""
    }

    fun deleteBytes(id: String) {
        COLLECTION?.deleteOne(Document("_id", id))
    }

    fun storeBytesCustom(collection: MongoCollection<Document>, bytes: ByteArray): String {
        val document = Document("bytes", Binary(bytes))
        collection.insertOne(document)
        return document.getObjectId("_id").toString()
    }
}
