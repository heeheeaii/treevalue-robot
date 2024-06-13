package com.treevalue.robot.resource.mongodb.util

import com.google.common.reflect.TypeToken
import com.google.gson.Gson

object JsonUtil {
    val gson = Gson()
    fun jsonStringToHashMap(json: String): Map<String, Any> {
        return gson.fromJson(json, object : TypeToken<HashMap<String?, Any?>?>() {}.getType())
    }

    fun <T> mapToObj(map: Map<Any, Any>, obj: Class<T>): T {
        return gson.fromJson(gson.toJson(map),obj)
    }

}
