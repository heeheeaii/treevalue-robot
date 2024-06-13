package com.treevalue.robot.resource.web.controller

import com.treevalue.robot.resource.es8.repository.Es8ResourceSearchInfoService
import com.treevalue.robot.resource.jpaes8.JpaResourceSearchInfoRepository
import com.treevalue.robot.resource.model.ResourceSearchInfo
import com.treevalue.robot.resource.remote.RemoteApi
import com.treevalue.robot.resource.remote.msg.SemanticsMsg
import com.treevalue.robot.resource.web.model.ResourceSearchResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/search")
class SearchController(
    private val jpaResourceSearchInfoRepository: JpaResourceSearchInfoRepository,
    private val remoteApi: RemoteApi,
    private val es8ResourceSearchInfoService: Es8ResourceSearchInfoService
) {
    @GetMapping("/search")
    fun putPreview(@RequestParam("q") q: String): ResponseEntity<List<ResourceSearchResult?>> {
        val semantics: SemanticsMsg = remoteSemantics(q)
        val res: List<ResourceSearchResult?> = esSearch(semantics)
        return ResponseEntity.ok().body(res)
    }

    @GetMapping("/test")
    fun test():String{
        return "ok"
    }

    @PostMapping("/test/search")
    fun esSearchTest(@RequestBody semanticsMsg: SemanticsMsg): List<ResourceSearchInfo?> {
        val resourceInfos =
            es8ResourceSearchInfoService.findTop10ByUsefulnessAndCondition_v2(semanticsMsg.getUsefulnessAsString(),semanticsMsg.getConditionsAsString())
        return resourceInfos
    }

    private fun esSearch(semanticsMsg: SemanticsMsg): List<ResourceSearchResult?> {
        val resourceInfos =
            es8ResourceSearchInfoService.findByUsefulnessAndConditionReturnResourceInfo(semanticsMsg)
        val res = ArrayList<ResourceSearchResult>()
        resourceInfos.forEach{
            val preview = remoteApi.getResourcePreview(it!!.resourceId)
            val priceAndEvaluation = remoteApi.getResourcePriceAndEvaluation(it.resourceId)
            val feedbackLevel = remoteApi.getResourceFeedbackLevel(it.resourceId)
            res.add(ResourceSearchResult(it.resourceId,preview,it.condition,it.usefulness,feedbackLevel,priceAndEvaluation.price,priceAndEvaluation.evaluation))
        }
        return res
    }

       private fun remoteSemantics(q: String): SemanticsMsg {
        return remoteApi.getPhraseSemantics(q)
    }


}
