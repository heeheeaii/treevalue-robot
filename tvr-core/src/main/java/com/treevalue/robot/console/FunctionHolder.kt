package com.treevalue.robot.console

object FunctionHolder {
    fun printTest() {
        println("test successfully")
    }

    val functionMap: Map<String, () -> Unit> = mapOf(
        FunctionHolder::printTest.name to FunctionHolder::printTest,
    )

    @JvmStatic
    fun main(args: Array<String>) {
        functionMap["printTest"]?.let { it() }
    }
}
