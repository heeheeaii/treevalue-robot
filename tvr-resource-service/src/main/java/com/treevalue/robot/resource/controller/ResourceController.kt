package com.treevalue.robot.resource.controller

import com.treevalue.robot.resource.mongodb.saveResourceInfoPackage.ResourceInfoPackageRepository
import com.treevalue.robot.resource.mongodb.saveless16mresource.ResourceByteRepository
import com.treevalue.robot.resource.mongodb.util.JsonUtil
import com.treevalue.robot.resource.pgsql.mapper.ResourceDescriptionMapper
import com.treevalue.robot.resource.pgsql.mapper.ResourceFeedbackEvaluationMapper
import com.treevalue.robot.resource.pgsql.mapper.ResourceFeedbackMapper
import com.treevalue.robot.resource.pgsql.msg.ResourcePriceAndEvaluationMsg
import com.treevalue.robot.resource.web.changer.MsgToData
import com.treevalue.robot.resource.web.msg.WebResourcePackage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/resource")
class ResourceController(
    private val resourceInfoPackageRepository: ResourceInfoPackageRepository,
    private val resourceByteRepository: ResourceByteRepository,
    private val resourceDescriptionMapper: ResourceDescriptionMapper,
    private val resourceFeedbackMapper: ResourceFeedbackMapper,
    private val resourceFeedbackEvaluationMapper: ResourceFeedbackEvaluationMapper,
    private val msgToData: MsgToData
) {
    @GetMapping("/test")
    fun putPreview(): String {
        return "ok"
    }
    @GetMapping("/preview/{resourceId}")
    fun getPreviewByResourceId(@PathVariable("resourceId") id:String):ByteArray?{
        return resourceByteRepository.findByResourceId(id)
    }

    @GetMapping("/priceAndEvaluation/{resourceId}")
    fun getPriceAndEvaluation(@PathVariable("resourceId") resourceId:String):ResourcePriceAndEvaluationMsg?{
        return resourceDescriptionMapper.findPriceAndEvaluationByResourceId(resourceId);
    }

    @GetMapping("/feedbackLevel/{resourceId}")
    fun getFeedbackLevel(@PathVariable("resourceId") resourceId: String):Double?{
        return resourceFeedbackEvaluationMapper.findEvaluationByResourceId(resourceId)
    }

    @PostMapping("/submit")
    fun saveResource(@RequestBody body: String): ResponseEntity<Unit> {
        val map = JsonUtil.jsonStringToHashMap(body)
        val webResourcePackage = JsonUtil.mapToObj(map["_rawValue"] as Map<Any, Any>, WebResourcePackage::class.java)
        val resourceInfoPackage = msgToData.changeToResourceInfoPackage(webResourcePackage)
        resourceInfoPackageRepository.save(resourceInfoPackage)
        return ResponseEntity.ok().body(Unit)
    }
}
