package com.treevalue.robot.v3.other.data

class Tensor<T> : Iterator<T> {
    lateinit var shape: List<Int>
        private set
    private lateinit var data: MutableList<T>
    private var itorIdx = 0;
    var size = 0;

    constructor() {
        throw Exception("can't use space constructor function")
    }

    constructor(shape: List<Int>, data: MutableList<T>) {
        size = shape.reduce { acc, it -> acc + it }
        require(size == data.size) { "data number size error" }
        this.shape = shape
        this.data = data

    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Tensor<*>) return false
        if (this === other) return true
        var idx = 0;
        for (itm in other) {
            if (idx < data.size) {
                if (itm != data[idx++]) return false
            }
        }
        return true
    }

    override fun hasNext(): Boolean {
        return itorIdx < data.size
    }

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        return data[itorIdx++]
    }


    //    init {
//        require(shape.isNotEmpty()) { "Shape must not be empty" }
//        val expectedSize = shape.reduce { acc, i -> acc * i }
//        require(data.size == expectedSize) { "Data size does not match shape" }
//    }
//
    operator fun get(vararg indices: Int): T {
        require(indices.size == shape.size) { "Number of indices must match the tensor's shape" }
        val index = calculateIndex(indices)
        return data[index]
    }

    operator fun set(vararg indices: Int, value: T) {
        require(indices.size == shape.size) { "Number of indices must match the tensor's shape" }
        val index = calculateIndex(indices)
        data[index] = value
    }

    private fun calculateIndex(indices: IntArray): Int {
        var index = 0
        var multiplier = 1
        for (i in indices.indices.reversed()) {
            index += indices[i] * multiplier
            multiplier *= shape[i]
        }
        return index
    }

    override fun toString(): String {
        return buildString {
            append("Tensor(shape=$shape, data=$data)")
        }
    }

    private fun <T> checkToPrintStringRoStringBuilder(
        index: Int,
        stringBuilder: StringBuilder,
        dataSource: MutableList<T>,
        lens: MutableList<Int>,
        begPosition: Int
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

    private fun <T> toPrintStringRoStringBuilder(
        index: Int,
        stringBuilder: StringBuilder,
        dataSource: MutableList<T>,
        lens: MutableList<Int>,
        begPosition: Int
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
                    if (idx != lens[index] - 1) stringBuilder.append(", ")
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


    fun toPrintString(): String {
        TODO()
    }


    companion object {
        fun <T> create(shape: List<Int>, initializer: (IntArray) -> T): Tensor<T> {
            val size = shape.reduce { acc, i -> acc * i }
            val data = MutableList<T>(size) { initializer(IntArray(shape.size)) }
            val tensor = Tensor(shape, data)
            fillTensor(tensor, shape, initializer)
            return tensor
        }

        private fun <T> fillTensor(tensor: Tensor<T>, shape: List<Int>, initializer: (IntArray) -> T) {
            val totalElements = shape.reduce { acc, i -> acc * i }
            val indices = IntArray(shape.size)

            for (i in 0 until totalElements) {
                var temp = i
                for (j in shape.indices.reversed()) {
                    indices[j] = temp % shape[j]
                    temp /= shape[j]
                }
                tensor.set(*indices, value = initializer(indices))
            }

        }

    }

}
/*
fun main() {
    val tensor = Tensor.create(listOf(2, 3, 4)) { indices ->
        0
    }

    println(tensor)

    println(tensor[1, 2, 3])
    tensor[1, 2, 3] = 99
    println(tensor[1, 2, 3])
}
*/
