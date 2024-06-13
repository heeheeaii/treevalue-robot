package com.treevalue.robot.controller

import com.treevalue.robot.IOcheck.MultipartFileCheck
import com.treevalue.robot.data.DataMessage
import com.treevalue.robot.record.PreviewString
import com.treevalue.robot.record.ResourceExtension
import com.treevalue.robot.service.PreviewServie
import com.treevalue.robot.util.MongodbUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/preview")
class PreviewController() {
    @Autowired
    lateinit var previewServie: PreviewServie

    @Autowired
    lateinit var mongodbUtil: MongodbUtil

    @GetMapping("/test")
    fun t(): String {
        return "ok"
    }



    @PostMapping("/picPreview")
    fun postPreview(
        @RequestHeader("type") type: String,
        @RequestHeader("width") width: Int,
        @RequestHeader("height") height: Int,
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<ByteArray> {
        var res: ByteArray?
        val byte = file.bytes
        res = previewServie.getPreview(file.contentType, width, height, byte)
//        res = previewServie.pngJpegGeneratePreview(width, height, byte)
        val previewId = mongodbUtil.storeBytes(res)
        val sourceId = mongodbUtil.storeBytes(byte)
        val headers = HttpHeaders()
        headers.set("Access-Control-Expose-Headers","previewId,resourceId")
        headers.set("previewId", previewId)
        headers.set("resourceId", sourceId)
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(res)
    }

    @PutMapping("/preview/png/{width}/{height}")
    @Deprecated(message = "too complex")
    // width , height :pixel number
    fun putPreview(
        @PathVariable("width") width: Int,
        @PathVariable("height") height: Int,
        @RequestParam("file") pngImg: MultipartFile,
    ): DataMessage {
        val map = HashMap<HashMap<String, String>, ByteArray>()
        val key = HashMap<String, String>()
        if (!MultipartFileCheck.sizeCheck(ResourceExtension.PNG.ext, pngImg.bytes)) {
            return DataMessage(HttpStatus.BAD_REQUEST.value(), "file size is too larage", null)
        }
        key[PreviewString.NAME] = ResourceExtension.PNG.ext
        key[PreviewString.BYTE_TYPE] = ResourceExtension.PNG.ext
        key[PreviewString.WIDTH] = width.toString()
        key[PreviewString.HEIGHT] = height.toString()
        map[key] = pngImg.bytes
        val first: ByteArray? = previewServie.getPreview(map).first()?.resource
        return DataMessage(HttpStatus.OK.value(), "", first)
    }


}
