package com.treevalue.robot.resource.es8.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI


@Configuration
open class ElasticsearchConfig {
    @Value("\${elasticsearch.uri}")
    private val elasticsearchUrl: String? = null
    @Bean
    @Throws(Exception::class)
    open fun elasticsearchClient(): ElasticsearchClient {
        val uri = URI.create(elasticsearchUrl)
        val restClient = RestClient.builder(HttpHost(uri.host, uri.port, uri.scheme)).build()
        val transport = RestClientTransport(restClient, JacksonJsonpMapper())
        return ElasticsearchClient(transport)
    }
}
