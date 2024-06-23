package com.treevalue.robot.pointSet
object MatrixUtil {
    fun <T> transpose(matrix: Array<Array<T>>): List<List<T?>> {
        val rows = matrix.size
        val cols = matrix[0].size
        val transposedMatrix = List(cols) { MutableList<T?>(rows) { null } }
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                transposedMatrix[j][i] = matrix[i][j]
            }
        }
        return transposedMatrix
    }
}
