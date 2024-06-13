package com.treevalue.robot.global

import java.io.File

object GlobalData {
    val SEPARATOR = File.separator
    val CUR_DIRECTORY = ".${SEPARATOR}"

    val VISUALMOMENTPOOL_MAX_WAIT = 20
    val MACHINE_CORE_NUMBER = 8

    val VISUAL_COUNTER: Long = Long.MIN_VALUE
    val VISUAL_OBJ_COUNTER_STORAGE_POSITION="D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\resources\\modelmachine\\visual\\visual_obj_counter"
}
