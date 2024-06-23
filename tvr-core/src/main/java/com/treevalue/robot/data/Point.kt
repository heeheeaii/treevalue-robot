package com.treevalue.robot.data

data class Point<K : Number, T : Number>(var first: K, var second: T) {
    override fun toString(): String {
        return "($first, $second)"
    }
}
