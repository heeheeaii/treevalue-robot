package com.treevalue.test.command

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException


class CommandLineParser {
    @Parameter(names = ["--help", "-h"], help = true, description = "display help info")
    private var help = false

    @Parameter(names = ["--version", "-v"], description = "display version info")
    private var version = false

    @Parameter(names = ["--file", "-f"], description = "files that need to be processed")
    private var file: String? = null


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = CommandLineParser()
            val jc = JCommander.newBuilder()
                .addObject(main)
                .build()
            try {
                jc.parse(*args)
                if (main.help) {
                    jc.usage()
                    return
                }
                if (main.version) {
                    println("Version 1.0")
                    return
                }
                if (main.file != null) {
                    println("Processing file: " + main.file)
                } else {
                    println("No file provided. Use -f or --file to specify a file.")
                }
            } catch (e: ParameterException) {
                e.printStackTrace()
                jc.usage()
            }
        }
    }
}
