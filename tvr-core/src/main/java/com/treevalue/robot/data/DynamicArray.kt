package com.treevalue.robot.data

class DynamicArray<T : Any>(private val clazz: Class<T>,initialCapacity: Int = 10) : Iterable<T> {
    private var array: Array<T?> = java.lang.reflect.Array.newInstance(clazz, initialCapacity) as Array<T?>
    private var size = 0

    fun add(data: List<T>) {
        array = java.lang.reflect.Array.newInstance(clazz, data.size) as Array<T?>
        for (idx in data.indices) {
            array[idx] = data[idx]
        }
    }

    fun indices(): IntRange {
        return array.indices
    }

    fun add(element: T) {
        ensureCapacity(size + 1)
        array[size] = element
        size++
    }

    fun get(index: Int): T {
        if (index >= size || index < 0) {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
        @Suppress("UNCHECKED_CAST")
        return array[index] as T
    }

    fun getArray(): Array<T?> {
        return array
    }

    fun remove(index: Int): T {
        if (index >= size || index < 0) {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
        @Suppress("UNCHECKED_CAST")
        val oldElement = array[index] as T
        val numMoved = size - index - 1
        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved)
        }
        array[--size] = null
        return oldElement
    }

    private fun ensureCapacity(minCapacity: Int) {
        if (minCapacity - array.size > 0) {
            grow(minCapacity)
        }
    }

    private fun grow(minCapacity: Int) {
        val oldCapacity = array.size
        var newCapacity = oldCapacity + (oldCapacity shr 1) // 1.5 times the old capacity
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity
        }
        array = array.copyOf(newCapacity) as Array<T?>
    }

    fun size() = size

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var current = 0

        override fun hasNext(): Boolean = current < size

        override fun next(): T {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            @Suppress("UNCHECKED_CAST")
            return array[current++] as T
        }
    }
}
