package com.treevalue.robot.resource.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "tvr_resource_search_info")
@JsonIgnoreProperties(ignoreUnknown = true)
data class ResourceSearchInfo (
    @Id
    var id:Long?,
    @Field(type = FieldType.Text)
    var usefulness:String,
    var condition:List<String>,
    @Field(type = FieldType.Keyword)
    var resourceId:String
){
    companion object {
        const val INDEX_NAME = "tvr_resource_search_info"
        const val USEFULNESS="usefulness"
        const val CONDITION="condition"
    }
    constructor() : this(null,"", arrayListOf(""),"")
}
