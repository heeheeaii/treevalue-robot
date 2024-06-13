package com.treevalue.robot.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.connection.ConnectionPoolSettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@Configuration
open class MongodbConfig {

    @Bean
    open fun mongoClient(): MongoClient {
        val connectionString = ConnectionString("mongodb://mg1:mg1@localhost:27017/tmp")

        val codecRegistry = MongoClientSettings.getDefaultCodecRegistry()

        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .applyToConnectionPoolSettings { builder: ConnectionPoolSettings.Builder ->
                builder.maxSize(50)
                    .minSize(15)
                    .maxConnectionIdleTime(5, TimeUnit.SECONDS)
                    .maxConnectionLifeTime(7, TimeUnit.SECONDS)
            }
            .build()

        return MongoClients.create(settings)
    }
}

