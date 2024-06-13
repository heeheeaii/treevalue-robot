package com.treevalue.robot.api.http.controller

import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mongodb")
class TvrCoreController {

    @PutMapping("/preview/png")
    fun putPreview(@RequestBody pngImg: ByteArray): String {
        return ""
    }


}
