package com.treevalue.robot.console

import java.util.*


object Console {
    private val functionMap: Map<String, () -> Unit> = FunctionHolder.functionMap

    private fun help() {
        println(
            """
               
           """.trimIndent()
        )
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val scanner = Scanner(System.`in`)
        var running = true
        println("Welcome to the treevalue robot 1.0!")
        while (running) {
            print(">>")
            val input = scanner.nextLine()
            when (input) {
                "help" -> help()
                "exit" -> {
                    running = false
                }

                else -> println("Unknown command. input 'help' to get detail command")
            }
        }
        scanner.close()
        println("finish program")
    }
}

