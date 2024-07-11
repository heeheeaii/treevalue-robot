package com.treevalue.robot.ext


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


fun quickSort(arr: Array<Any>, property: String, low: Int, high: Int) {
    if (low < high) {
        val pi = partition(arr, property, low, high)
        quickSort(arr, property, low, pi - 1)
        quickSort(arr, property, pi + 1, high)
    }
}

fun partition(arr: Array<Any>, property: String, low: Int, high: Int): Int {
    val pivot = getProperty(arr[high], property) as Comparable<Any>
    var i = low - 1
    for (j in low until high) {
        if ((getProperty(arr[j], property) as Comparable<Any>) <= pivot) {
            i++
            arr[i] = arr[j].also { arr[j] = arr[i] }
        }
    }
    arr[i + 1] = arr[high].also { arr[high] = arr[i + 1] }
    return i + 1
}

fun getProperty(obj: Any, property: String): Any? {
    val clazz = obj.javaClass
    val field = clazz.getDeclaredField(property)
    field.isAccessible = true
    return field.get(obj)
}

fun heapSort(arr: Array<Any>, property: String) {
    val n = arr.size
    for (i in n / 2 - 1 downTo 0) {
        heapify(arr, n, i, property)
    }
    for (i in n - 1 downTo 0) {
        arr[0] = arr[i].also { arr[i] = arr[0] }
        heapify(arr, i, 0, property)
    }
}

fun heapify(arr: Array<Any>, n: Int, i: Int, property: String) {
    var largest = i
    val left = 2 * i + 1
    val right = 2 * i + 2
    if (left < n && (getProperty(arr[left], property) as Comparable<Any>) > (getProperty(
            arr[largest],
            property
        ) as Comparable<Any>)
    ) {
        largest = left
    }
    if (right < n && (getProperty(arr[right], property) as Comparable<Any>) > (getProperty(
            arr[largest],
            property
        ) as Comparable<Any>)
    ) {
        largest = right
    }
    if (largest != i) {
        arr[i] = arr[largest].also { arr[largest] = arr[i] }
        heapify(arr, n, largest, property)
    }
}
/*
data class Person(val name: String, val age: Int)
fun main() {
    val people = arrayOf(
        Person("Alice", 30),
        Person("Bob", 25),
        Person("Charlie", 35)
    ) as Array<Any>

    quickSort(people, "age", 0, people.size - 1)
    println((people as Array<Person>).joinToString { it.name })  // Sorted by name

    heapSort(people, "name")
    println((people as Array<Person>).joinToString { it.name })  // Sorted by name
}
 */
