package com.treevalue.robot.resource.mongodb.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.gridfs.GridFsTemplate

@Configuration
open class MongoConfig {

    @Bean
    open fun mongoTemplate(mongoDatabaseFactory: MongoDatabaseFactory): MongoTemplate {
        return MongoTemplate(mongoDatabaseFactory)
    }

    @Bean
    open fun gridFsTemplate(mongoDatabaseFactory: MongoDatabaseFactory): GridFsTemplate {
        return GridFsTemplate(mongoDatabaseFactory, MongoTemplate(mongoDatabaseFactory).converter)
    }
}
