package com.treevalue.robot.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogUtil(cfgPath: String = "src/main/resources/logback.xml") {
    private val logger: Logger = LoggerFactory.getLogger(LogUtil::class.java)

    init {
        System.setProperty("logback.configurationFile", cfgPath)
    }

    fun getLogger(): Logger {
        return logger
    }
}
