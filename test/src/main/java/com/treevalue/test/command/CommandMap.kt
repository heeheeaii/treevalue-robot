package com.treevalue.test.command

import com.beust.jcommander.JCommander

class CommandMap {
    private val parser: CommandLineParser = CommandLineParser()
    private val mapper: JCommander = JCommander.newBuilder()
        .addObject(parser)
        .build()

    fun parse(cmd: String): String {
        mapper.parse(cmd)
        var rst = call()
        return rst ?: "{}"
    }

    fun call(): String? {
        return "{}"
    }
}
