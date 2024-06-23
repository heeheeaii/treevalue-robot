package com.treevalue.robot.algorithm


// keep order in the same value

/*
    sort 1 inc, -1 dec
 */
fun mergeSort(entries: Array<Map.Entry<String, Int>>, sort: Int = 1): Array<Map.Entry<String, Int>> {
    assert(sort in -1..1 step 2)
    if (entries.size <= 1) return entries
    val middle = entries.size / 2
    val left = entries.sliceArray(0 until middle)
    val right = entries.sliceArray(middle until entries.size)
    return merge(mergeSort(left, sort), mergeSort(right, sort), sort)
}

fun merge(
    left: Array<Map.Entry<String, Int>>, right: Array<Map.Entry<String, Int>>, sort: Int
): Array<Map.Entry<String, Int>> {
    var i = 0
    var j = 0
    val result = mutableListOf<Map.Entry<String, Int>>()
    while (i < left.size && j < right.size) {
        if ((left[i].value <= right[j].value && sort == 1) || (left[i].value >= right[j].value && sort == -1)) {
            result.add(left[i])
            i++
        } else {
            result.add(right[j])
            j++
        }
    }
    result.addAll(left.slice(i until left.size))
    result.addAll(right.slice(j until right.size))
    return result.toTypedArray()
}

// don't keep order in the same value
fun quickSort(entries: Array<Map.Entry<String, Int>>, low: Int = 0, high: Int = entries.size - 1) {
    if (low < high) {
        val pi = partition(entries, low, high)
        quickSort(entries, low, pi - 1)
        quickSort(entries, pi + 1, high)
    }
}

fun partition(entries: Array<Map.Entry<String, Int>>, low: Int, high: Int): Int {
    val pivot = entries[high].value
    var i = low - 1
    for (j in low until high) {
        if (entries[j].value <= pivot) {
            i++
            entries.swap(i, j)
        }
    }
    entries.swap(i + 1, high)
    return i + 1
}

fun Array<Map.Entry<String, Int>>.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}
