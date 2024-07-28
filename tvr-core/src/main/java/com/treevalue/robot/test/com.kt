package com.treevalue.robot.test

class MyClass {
    var myProperty: String
        private set // 使 setter 私有化，外部无法修改
        get // 默认 getter

    constructor(initialValue: String) {
        myProperty = initialValue
    }
}

fun main() {
    val a = intArrayOf(1, 3, 4)
    println(a.toString())
}
