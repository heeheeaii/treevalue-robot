package com.treevalue.robot.resource.remote.msg

import java.io.Serializable

data class SemanticsMsg(
    val usefulness: Array<String>,
    val conditions: Array<String>
):Serializable {
    constructor():this(
        arrayOf(""), arrayOf("")
    )
    fun getUsefulnessAsString(): String {
        val res = StringBuilder()
        for (itm in usefulness) {
            res.append(itm)
            res.append(" ")
        }
        return res.toString()
    }

    fun getConditionsAsString(): String {
        val res = StringBuilder()
        for (itm in conditions) {
            res.append(itm)
            res.append(" ")
        }
        return res.toString()
    }
}
