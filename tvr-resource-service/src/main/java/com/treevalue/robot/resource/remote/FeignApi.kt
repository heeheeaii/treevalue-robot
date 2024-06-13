package com.treevalue.robot.resource.remote

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient("tvr-resource", url = "http://localhost:10002")
@Service("feignApi")
interface FeignApi {
    @GetMapping("/users/tokenToId/{token}")
    fun parseTokenToUserId(@PathVariable("token") token: String): ResponseEntity<String>
}
