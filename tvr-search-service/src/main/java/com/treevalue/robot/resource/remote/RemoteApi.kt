package com.treevalue.robot.resource.remote

import com.treevalue.robot.resource.global.RemoteAddress
import com.treevalue.robot.resource.remote.msg.ResourcePriceAndEvaluation
import com.treevalue.robot.resource.remote.msg.SemanticsMsg
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class RemoteApi(
    val restClient: RestTemplate
) {
    fun getPhraseSemantics(q: String): SemanticsMsg {
        val searchUrl = RemoteAddress.PY_SEMITICS_HTTP + q
        val response = restClient.getForObject(searchUrl, SemanticsMsg::class.java)
        return response
    }

    fun getResourcePreview(resourceId: String): ByteArray {
        val previewUrl = UriComponentsBuilder.fromUriString(RemoteAddress.MONGO_PREVIEW_BY_RESOURCEID)
            .buildAndExpand(resourceId).toUriString()
        val res = restClient.getForObject(previewUrl, ByteArray::class.java)
        return res
    }

    fun getResourcePriceAndEvaluation(resourceId: String): ResourcePriceAndEvaluation {
        val priceAndEvaluationUrl = UriComponentsBuilder.fromUriString(RemoteAddress.PG_RESOURCE_PRICE_EVALUATION)
            .buildAndExpand(resourceId).toUriString()
        val res = restClient.getForObject(priceAndEvaluationUrl, ResourcePriceAndEvaluation::class.java)
        return res
    }

    fun getResourceFeedbackLevel(resourceId: String): Double {
        val feedbackEvaluationUrl = UriComponentsBuilder.fromUriString(RemoteAddress.PG_RESOURCE_FEEDBACK_EVALUATION)
            .buildAndExpand(resourceId).toUriString()
        val res = restClient.getForObject(feedbackEvaluationUrl, Double::class.java)
        return res
    }
}
