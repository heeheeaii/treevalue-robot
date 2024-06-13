package com.treevalue.robot.service

import com.treevalue.robot.data.RetResourceOrPreview
import kotlin.collections.HashMap

interface PreviewServie {
    // pic video sound text pdf
    fun getPreview(waitThings: HashMap<HashMap<String, String>, ByteArray>): ArrayList<RetResourceOrPreview>

    fun getPreview(type: String, width: Int, height: Int, bytes: ByteArray): ByteArray

    fun pngJpegGeneratePreview(width: Int, height: Int, bytes: ByteArray): ByteArray

}
