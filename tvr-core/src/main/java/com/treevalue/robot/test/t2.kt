package com.treevalue.robot.test

class IntRangeIterator(private val start: Int, private val end: Int) : Iterator<Int> {
    private var current = start

    override fun hasNext(): Boolean {
        return current <= end
    }

    override fun next(): Int {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return current++
    }
}
