package com.treevalue.robot.v2.test

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.assertFailsWith

class ToPrintStringRoStringBuilderTest {

    fun <T> checkToPrintStringRoStringBuilder(
        index: Int, stringBuilder: StringBuilder, dataSource: MutableList<T>, lens: MutableList<Int>, begPosition: Int
    ): Boolean {
        if (index < lens.size) {
            var all = 1
            lens.forEach {
                if (it <= 0) return false
                all *= it
            }
            if (dataSource.size == all) return true
        }
        return false
    }

    fun <T> toPrintStringRoStringBuilder(
        index: Int, stringBuilder: StringBuilder, dataSource: MutableList<T>, lens: MutableList<Int>, begPosition: Int
    ) {
        if (checkToPrintStringRoStringBuilder(index, stringBuilder, dataSource, lens, begPosition)) {
            stringBuilder.append("[")
            for (idx in 0 until lens[index]) {
                if (index < lens.size - 1) {
                    var step = 1
                    for (i in index + 1 until lens.size) {
                        step *= lens[i]
                    }
                    toPrintStringRoStringBuilder(index + 1, stringBuilder, dataSource, lens, begPosition + idx * step)
                    if (idx != lens[index] - 1) stringBuilder.append(", ") // 添加逗号分隔符
                } else {
                    stringBuilder.append(dataSource[begPosition + idx])
                    if (idx != lens[index] - 1) stringBuilder.append(", ")
                }
            }
            stringBuilder.append("]")
            return
        }
        throw IllegalArgumentException()
    }

    fun testValidInput() {
        val dataSource = mutableListOf("A", "B", "D", "E", "F", "G", "H", "I", "J", "K", "I", "J")
        val lens = mutableListOf(3, 2, 2)
        val stringBuilder = StringBuilder()
        toPrintStringRoStringBuilder(0, stringBuilder, dataSource, lens, 0)
        assertEquals("[[[A, B], [D, E]], [[F, G], [H, I]], [[J, K], [I, J]]]", stringBuilder.toString())
    }

    fun testInvalidInput_LensLessThanZero() {
        val dataSource = mutableListOf("A", "B", "D", "E", "F", "G", "H", "I", "J", "K")
        val lens = mutableListOf(3, -2, 2)
        val stringBuilder = StringBuilder()
        assertFailsWith<IllegalArgumentException> {
            toPrintStringRoStringBuilder(0, stringBuilder, dataSource, lens, 0)
        }
    }

    fun testInvalidInput_DataSourceSizeMismatch() {
        val dataSource = mutableListOf("A", "B", "D", "E", "F", "G", "H", "I", "J")
        val lens = mutableListOf(3, 2, 2)
        val stringBuilder = StringBuilder()
        assertFailsWith<IllegalArgumentException> {
            toPrintStringRoStringBuilder(0, stringBuilder, dataSource, lens, 0)
        }
    }

//    fun printTest() {
//        val dataSource = mutableListOf("A", "B", "D", "E", "F", "G", "H", "I", "J", "K", "I", "J")
//        val lens = mutableListOf(3, 2, 2)
//        val stringBuilder = StringBuilder()
//        toPrintStringRoStringBuilder(0, stringBuilder, dataSource, lens, 0)
//        printMatrixString(stringBuilder.toString())
//        assertEquals(
//            """[[[[1, 2],    [[[9, 10],
//                  [3, 4]],  	[11, 12]],
//                 [[5, 6],     [[13, 14],
//                  [7, 8]]],    [15, 16]]]]""".trimMargin(), stringBuilder.toString()
//        )

//    }

    private fun printMatrixString(matrix: String): String {
        TODO("Not yet implemented")
    }

}

fun testCase1() {
    val v = ToPrintStringRoStringBuilderTest()
    v.testInvalidInput_LensLessThanZero()
    v.testInvalidInput_DataSourceSizeMismatch()
    v.testValidInput()
}

fun main() {

}
