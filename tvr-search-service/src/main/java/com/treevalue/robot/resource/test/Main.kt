package com.treevalue.robot.resource.test

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Main {

    fun generateJson(data: List<Any?>?): String {
        val gson: Gson = GsonBuilder().create()
        return gson.toJson(data)
    }

}
