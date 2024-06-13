package com.treevalue.robot.resource.es8.repository


import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty
import co.elastic.clients.elasticsearch._types.mapping.Property
import co.elastic.clients.elasticsearch._types.mapping.TextProperty
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping
import co.elastic.clients.elasticsearch._types.query_dsl.*
import co.elastic.clients.elasticsearch.core.BulkRequest
import co.elastic.clients.elasticsearch.core.BulkResponse
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest
import com.treevalue.robot.resource.global.LogUtil
import com.treevalue.robot.resource.jpaes8.JpaResourceSearchInfoRepository
import com.treevalue.robot.resource.model.ResourceSearchInfo
import com.treevalue.robot.resource.remote.msg.SemanticsMsg
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service


@Service
class Es8ResourceSearchInfoService(
    private val client: ElasticsearchClient,
    private val jpaResourceSearchInfoRepository: JpaResourceSearchInfoRepository
) {
    fun checkExist(): Boolean {
        return client.indices().exists { e -> e.index(ResourceSearchInfo.INDEX_NAME) }.value()
    }

    @PostConstruct
    fun initIndex() {
        val index = ResourceSearchInfo.INDEX_NAME
        val exists: Boolean = client.indices().exists { e ->
            e.index(index)
        }.value()
        if (!exists) {
            val textProperty = TextProperty.Builder().index(true).build()
            val keywordProperty = KeywordProperty.Builder().index(true).build()
            val mappingProperties = mapOf(
                "usefulness" to Property.Builder().text(textProperty).build(),
                "condition" to Property.Builder().text(textProperty).build(),
                "resourceId" to Property.Builder().keyword(keywordProperty).build(),
            )
            val typeMapping = TypeMapping.Builder().properties(mappingProperties).build()
            val createRequest = CreateIndexRequest.Builder().index(index).mappings(typeMapping).build()
            client.indices().create(createRequest)
            LogUtil.LOGGER.info("create es index: ${ResourceSearchInfo.INDEX_NAME}")
        }
    }

    fun findByUsefulnessAndConditionReturnResourceInfo(
        semanticsMsg: SemanticsMsg
    ): List<ResourceSearchInfo?> {
        val res = findTop10ByUsefulnessAndCondition_v2(
            semanticsMsg.getUsefulnessAsString(),
            semanticsMsg.getConditionsAsString()
        )
        return res
    }

    fun findTop10ByUsefulnessAndCondition_v2(usefulness: String, condition: String): List<ResourceSearchInfo?> {
        val multi = MultiMatchQuery.Builder()
            .query("$usefulness $condition")
            .fields(ResourceSearchInfo.USEFULNESS, ResourceSearchInfo.CONDITION)
            .type(TextQueryType.BestFields)
            .tieBreaker(0.3)
            .operator(Operator.Or)
            .build()
        val multiQuery = Query.Builder().multiMatch(multi).build()
        val searchRequest =
            SearchRequest.Builder().index(ResourceSearchInfo.INDEX_NAME).query(multiQuery).size(10).from(0).build()
        val hits = client.search(searchRequest, ResourceSearchInfo::class.java)
        val res = ArrayList<ResourceSearchInfo?>()
        hits.hits().hits().forEach {
            res.add(it.source())
        }
        return res
    }

    @Deprecated("update to findTop10ByUsefulnessAndCondition_2")
    fun findTop10ByUsefulnessAndCondition(usefulness: String, condition: String): List<ResourceSearchInfo?> {
        val m1 = MatchQuery.Builder().field("usefulness").query(usefulness).build()
        val m2 = MatchQuery.Builder().field("condition").query(condition).build()
        val match1 = Query.Builder().match(m1).build()
        val match2 = Query.Builder().match(m2).build()
        val b1 = BoolQuery.Builder().should(match1, match2).build()
        val boolQuery = Query.Builder().bool(b1).build()
        val build =
            SearchRequest.Builder().index(ResourceSearchInfo.INDEX_NAME).query(boolQuery).from(0).size(10).build()
        val searchResponse = client.search(build, ResourceSearchInfo::class.java)
        val hits = searchResponse.hits().hits()
        val res = ArrayList<ResourceSearchInfo?>()
        hits.forEach {
            res.add(it.source())
        }
        return res
    }


    fun saveOne(resourceSearchInfo: ResourceSearchInfo) {
        val indexResponse = client.index { i: IndexRequest.Builder<ResourceSearchInfo> ->
            i.index(ResourceSearchInfo.INDEX_NAME).id(resourceSearchInfo.resourceId)
                .document(resourceSearchInfo)
        }
    }

    fun saveAll(list: List<ResourceSearchInfo>): Boolean {
        try{
            jpaResourceSearchInfoRepository.saveAll(list)
        }catch (e:NoSuchMethodError){
            LogUtil.LOGGER.info("version match has problem : can't fond boolean org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty.isReadable()")
        }
        return true
    }

    fun saveAll_old(list: List<ResourceSearchInfo>): Boolean {
        val bulkResponse: BulkResponse = client.bulk { b: BulkRequest.Builder ->
            b.operations(list.map { resource ->
                BulkOperation.of { bo: BulkOperation.Builder ->
                    bo.index<ResourceSearchInfo?> { i: IndexOperation.Builder<ResourceSearchInfo?> ->
                        i.index(ResourceSearchInfo.INDEX_NAME).id(resource.resourceId).document(resource)
                    }
                }
            })
        }
        return bulkResponse.errors()
    }

    fun deleteByResourceId(resourceId: String) {
        client.delete { d ->
            d.index(ResourceSearchInfo.INDEX_NAME).id(resourceId)
        }
    }
}
