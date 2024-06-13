package com.treevalue.robot.resource.web.controller

import com.treevalue.robot.resource.model.ResourceSearchInfo
import com.treevalue.robot.resource.es8.repository.Es8ResourceSearchInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class BehindSearchController {

    @Autowired
    private lateinit var es8ResourceSearchInfoService: Es8ResourceSearchInfoService

    @PostMapping("/save")
    fun saveResourceSearchInfo(@RequestBody resourceSearchInfo: ResourceSearchInfo) {
        es8ResourceSearchInfoService.saveOne(resourceSearchInfo)
    }

    @PostMapping("/saveBatch")
    fun saveBatchResourceSearchInfo(@RequestBody resourceSearchInfo: List<ResourceSearchInfo>): ResponseEntity<Nothing> {
        es8ResourceSearchInfoService.saveAll(resourceSearchInfo)
        return ResponseEntity(HttpStatus.OK)
    }
}
