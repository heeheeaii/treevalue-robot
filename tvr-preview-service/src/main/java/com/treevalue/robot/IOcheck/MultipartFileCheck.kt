package com.treevalue.robot.IOcheck

import org.springframework.util.unit.DataSize


object MultipartFileCheck {
    fun sizeCheck(fileExtension: String, body: ByteArray): Boolean {
        var res = true
        when (fileExtension) {
            "png" -> if (body.size > DataSize.ofMegabytes(15).toBytes()) {
                res = false
            }

            "txt" -> if (body.size > DataSize.ofMegabytes(15).toBytes()) {
                res = false
            }

            "mp4" -> if (body.size > DataSize.ofGigabytes(5).toBytes()) {
                res = false
            }

            "mp3" -> if (body.size > DataSize.ofMegabytes(30).toBytes()) {
                res = false
            }

            else -> throw RuntimeException("Unsupported file type: $fileExtension")
        }
        return res
    }
}
