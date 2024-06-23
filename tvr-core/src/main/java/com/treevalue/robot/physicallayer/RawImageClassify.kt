package com.treevalue.robot.physicallayer

import com.treevalue.robot.anno.Macro
import com.treevalue.robot.anno.TopApi
import com.treevalue.robot.data.Point
import com.treevalue.robot.data.TriPoint
import com.treevalue.robot.ext.getRgb
import com.treevalue.robot.math.RandomUtil
import com.treevalue.robot.pointSet.PointSetUtil
import java.awt.image.BufferedImage
import java.util.*

class RawImageClassify {
    private val MAX_DIFF = 0.05

    //    p(colorInt, mark)
    /* -> y -height
       |
       `'
       x - width

     */
    lateinit var markedImageMatrix: Array<Array<TriPoint<Int, Int>>>
    private var width: Int = 0
    private var height: Int = 0
    private var bounds: HashMap<Int, HashMap<Int, HashSet<Point<Int, Int>>>> = HashMap()

    constructor(img: BufferedImage) {
        classify(img)
    }

    fun resetImage(img: BufferedImage) {
        classify(img)
    }

    fun initImg(img: BufferedImage) {
        markedImageMatrix = Array(img.width) { Array(img.height) { TriPoint(0, Macro.INVALID) } }
        width = img.width
        height = img.height
        for (idx in 0 until img.width) {
            for (jdx in 0 until img.height) {
                markedImageMatrix[idx][jdx].rgb = img.getRGB(idx, jdx)
            }
        }
    }

    //    make img has individual visual object, by similar and relative algorithm
    @TopApi
    fun classify(img: BufferedImage) {
        initImg(img)
        similar(img)
        relative()
    }

    private fun relative() {
        val pointNumber = HashMap<Int, Int>()
//        todo : completly don't work
        val zones = HashMap<Int, HashMap<Int, HashSet<Point<Int, Int>>>>()
        statics(pointNumber, zones)
        val sortPoint = sortNode(pointNumber.entries.toTypedArray())
        disappear(sortPoint, 0.05f, 0, zones)
    }


    private fun disappear(
        sortPoints: Array<Map.Entry<Int, Int>>,
        threshold: Float,
        relatedMaxOrder: Int,
        zones: HashMap<Int, HashMap<Int, HashSet<Point<Int, Int>>>>
    ) {
        notSee()
        val pointsNumber: HashMap<Int, Int> = toHashMap(sortPoints)
        var minPoint = (sortPoints[relatedMaxOrder].value * threshold).toInt()
        var thresholdOrder = sortPoints.size - 1
        for (idx in sortPoints.indices) {
            if (sortPoints[idx].value == minPoint) {
                thresholdOrder = idx
                break
            }
        }
        for (idx in thresholdOrder until sortPoints.size) {
            val key = sortPoints[idx].key
            disappear(pointsNumber[key], zones[key], pointsNumber)
        }

    }

    private fun toHashMap(pointsNumber: Array<Map.Entry<Int, Int>>): HashMap<Int, Int> {
        val rst = HashMap<Int, Int>()
        pointsNumber.forEach { ety ->
            rst[ety.key] = ety.value
        }
        return rst
    }


    private fun disappear(
        mark: Int?, zones: HashMap<Int, HashSet<Point<Int, Int>>>?, pointsNumber: HashMap<Int, Int>
    ) {
        if (mark == null || zones == null) return
        val divide: IntArray = divideTo(pointsNumber[mark]!!, zones, pointsNumber)
        for (nmk in zones.keys) {
            divide(mark, pointsNumber[mark]!!, nmk, zones)
            pointsNumber.remove(mark)
            zones.remove(mark)
        }
    }

    private fun divide(omk: Int, all: Int, nmk: Int, zones: HashMap<Int, HashSet<Point<Int, Int>>>) {
        val probability = DoubleArray(8) { 1 / 8.0 }
        var countr = 0
        val nps: Queue<Point<Int, Int>> = LinkedList()
        for (pts in zones[omk]!!) {
            nps.add(pts)
        }
        val move = PointSetUtil.getMove()
        while (nps.size > 0 && countr < all) {
            val pot = nps.poll()
            var index = 0
            for (idx in probability.indices) {
                index = RandomUtil.geneIndexByProbability(probability)
                try {
                    val trp = markedImageMatrix[pot.first + move[2 * index]][pot.second + move[2 * idx + 1]]
                    if (trp.mark != nmk) {
                        trp.mark = nmk
                        nps.add(Point(pot.first + move[2 * index], pot.second + move[2 * idx + 1]))
                    } else {
                        dec(probability, index)
                    }
                    countr++
                    if (countr == all) break
                } catch (e: ArrayIndexOutOfBoundsException) {
                    continue
                }
            }
        }

    }

    private fun dec(probability: DoubleArray, index: Int) {
        val stp = probability[index] * 0.8 / (probability.size - 1)
        for (idx in probability.indices) {
            if (idx != index) {
                probability[idx] += stp
            } else {
                probability[idx] *= 0.2
            }
        }

    }

    private fun divideTo(
        all: Int, zones: HashMap<Int, HashSet<Point<Int, Int>>>, pointsNumber: HashMap<Int, Int>
    ): IntArray {
        val rst = IntArray(zones.size * 2) { 0 }
        var sum = 0
        zones.forEach { (kye, vlu) ->
            sum += pointsNumber[kye] ?: 0
        }
        var idx = 0
        for (key in zones.keys) {
            rst[idx * 2] = key
            rst[idx * 2 + 1] = (all * (pointsNumber[key] ?: 0) / sum.toDouble()).toInt()
            idx++
        }
        sum = 0
        for (idx in 0 until zones.size) {
            sum += rst[idx * 2 + 1]
        }
        if (sum != all) {
            rst[rst.size - 1] += all - sum
        }
        return rst
    }

    private fun notSee(dataMatrix: Array<Array<TriPoint<Int, Int>>> = this.markedImageMatrix) {
        dataMatrix.forEach { line ->
            line.forEach {
                it.visited = false
            }

        }
    }

    fun sortNode(points: Array<Map.Entry<Int, Int>>): Array<Map.Entry<Int, Int>> {
        if (points.size < 2) {
            return points
        }

        val middle = points.size / 2
        val left = points.copyOfRange(0, middle)
        val right = points.copyOfRange(middle, points.size)

        return merge(sortNode(left), sortNode(right))
    }

    private fun merge(left: Array<Map.Entry<Int, Int>>, right: Array<Map.Entry<Int, Int>>): Array<Map.Entry<Int, Int>> {
        var i = 0
        var j = 0
        var k = 0
        val merged = Array(left.size + right.size) { left[0] }

        while (i < left.size && j < right.size) {
            if (left[i].value <= right[j].value) {
                merged[k] = left[i]
                i++
            } else {
                merged[k] = right[j]
                j++
            }
            k++
        }

        while (i < left.size) {
            merged[k] = left[i]
            i++
            k++
        }

        while (j < right.size) {
            merged[k] = right[j]
            j++
            k++
        }

        return merged
    }


    fun count(
        x: Int,
        y: Int,
        pointNumber: HashMap<Int, Int> = HashMap(),
        dataMatrix: Array<Array<TriPoint<Int, Int>>> = markedImageMatrix,
    ) {
        val ctr = dataMatrix[x][y]
        pointNumber[ctr.mark] = pointNumber.getOrDefault(ctr.mark, 0) + 1
    }/*
    x beg x
    y beg y
    datamatrix points
    pointNumber mark number
    cs mark <mark, centerPoint>
     */

    //todo :still don't work completely
    private fun square3GridFindConnection(
        x: Int,
        y: Int,
        dataMatrix: Array<Array<TriPoint<Int, Int>>> = markedImageMatrix,
        pointNumber: HashMap<Int, Int> = HashMap(),
        connections: HashMap<Int, HashMap<Int, HashSet<Point<Int, Int>>>> = HashMap()
    ) {
        if (dataMatrix.isEmpty()) return
        if (dataMatrix.size - x >= 2 && dataMatrix[0].size - y >= 2) {
            val move = PointSetUtil.getMove()
            val cx = x + 1
            val cy = y + 1
            val cp = dataMatrix[cx][cy]
            val slider = Array<TriPoint<Int, Int>?>(3) { null }
            for (stp in 0..3) {
                val itn = 4 * stp
                val ctr = Point(cx + move[0 + itn], cy + move[1 + itn])
                val pre =
                    Point(cx + move[if (-2 + itn < 0) 14 else -2 + itn], cy + move[if (-1 + itn < 0) 15 else -1 + itn])
                val nxt = Point(cx + move[2 + itn], cy + move[3 + itn])
                var sx = ctr.first
                var sy = ctr.second
                slider[1] = if (sx >= dataMatrix.size || sy >= dataMatrix[0].size) null else dataMatrix[sx][sy]
                sx = pre.first
                sy = pre.second
                slider[0] = if (sx >= dataMatrix.size || sy >= dataMatrix[0].size) null else dataMatrix[sx][sy]
                sx = nxt.first
                sy = nxt.second
                slider[2] = if (sx >= dataMatrix.size || sy >= dataMatrix[0].size) null else dataMatrix[sx][sy]
                var cur: TriPoint<Int, Int>?
                for (idx in slider.indices) {
                    cur = slider[idx]
                    if (cur == null) continue
                    if (!cur.visited) {
                        cur.visited = true
                        pointNumber[cur.mark] = pointNumber.getOrDefault(cur.mark, 0) + 1
                        if (cur.mark != cp.mark) {
                            var ncdx = Macro.INVALID
                            var ncdy = Macro.INVALID
                            when (idx) {
                                0 -> {
                                    ncdx = pre.first
                                    ncdy = pre.second
                                }

                                1 -> {
                                    ncdx = ctr.first
                                    ncdy = ctr.second

                                }

                                2 -> {
                                    ncdx = nxt.first
                                    ncdy = nxt.second

                                }

                                else -> {}
                            }
                            dealConnect(cp, cur, connections, Point(ncdx, ncdy), cx, cy)
                        }
                        if (idx == 1) {
                            try {
                                val pot = dataMatrix[pre.first][pre.second]
                                dealConnect(cur, pot, connections, pre, ctr.first, ctr.second)
                            } catch (e: ArrayIndexOutOfBoundsException) {
                            }
                            try {
                                val pot = dataMatrix[nxt.first][nxt.second]
                                dealConnect(cur, pot, connections, nxt, ctr.first, ctr.second)
                            } catch (e: ArrayIndexOutOfBoundsException) {
                            }
                        }
                    }
                }
            }
        } else {
            var ctr: TriPoint<Int, Int>?
            var lst: TriPoint<Int, Int>?
            var fst: TriPoint<Int, Int>?
            var len = 2
            var pcx = 1 //point center x
            var pcy = 0
            if (dataMatrix.size >= len || dataMatrix[0].size >= len) {
                if (dataMatrix.size >= len) {
                    ctr = dataMatrix[pcx][pcy]
                    fst = dataMatrix[x][y]
                    try {
                        lst = dataMatrix[pcx + 1][pcy]
                        count(pcx + 1, pcy, pointNumber)
                        if (lst.mark != ctr.mark) {
                            dealConnect(ctr, lst, connections, pcx + 1, pcy, pcx, pcy)
                        }
                    } finally {
                        count(pcx, pcy, pointNumber)
                        count(pcx - 1, pcy, pointNumber)
                        if (ctr.mark != fst.mark) {
                            dealConnect(fst, ctr, connections, pcx, pcy, pcx - 1, pcy)
                        }
                    }
                }
                if (dataMatrix[0].size >= len) {
                    pcx = 0
                    pcy = 1
                    ctr = dataMatrix[pcx][pcy]
                    fst = dataMatrix[x][y]
                    try {
                        lst = dataMatrix[pcx][pcy + 1]
                        count(pcx, pcy + 1, pointNumber)
                        if (lst.mark != ctr.mark) {
                            dealConnect(ctr, lst, connections, pcx, pcy + 1, pcx, pcy)
                        }
                    } finally {
                        count(pcx, pcy, pointNumber)
                        count(pcx, pcy - 1, pointNumber)
                        if (ctr.mark != fst.mark) {
                            dealConnect(fst, ctr, connections, pcx, pcy, pcx, pcy - 1)
                        }
                    }
                }
            } else {
                try {
                    count(x, y, pointNumber, dataMatrix)
                } catch (e: ArrayIndexOutOfBoundsException) {
                }
            }
        }
    }

    private fun dealConnect(
        oneP: TriPoint<Int, Int>,
        other: TriPoint<Int, Int>,
        zones: HashMap<Int, HashMap<Int, HashSet<Point<Int, Int>>>>,
        otherX: Int,
        otherY: Int,
        oneX: Int,
        oneY: Int
    ) {
        if (oneP.mark != other.mark) {
            var map = zones.getOrDefault(oneP.mark, HashMap())
            var set = map.getOrDefault(other.mark, HashSet())
            set.add(Point(otherX, otherY))
            map[other.mark] = set
            zones[oneP.mark] = map
            map = zones.getOrDefault(other.mark, HashMap())
            set = map.getOrDefault(oneP.mark, HashSet())
            set.add(Point(oneX, oneY))
            map[oneP.mark] = set
            zones[other.mark] = map
        }
    }

    private fun dealConnect(
        oneP: TriPoint<Int, Int>,
        other: TriPoint<Int, Int>,
        zones: HashMap<Int, HashMap<Int, HashSet<Point<Int, Int>>>>,
        otherXY: Point<Int, Int>,
        oneX: Int,
        oneY: Int
    ) {
        dealConnect(oneP, other, zones, otherXY.first, otherXY.second, oneX, oneY)
    }


    /*
    pN: mark, total
    zs: mark, <otherMark,boundList>
     */
    private fun statics(
        pointNumber: HashMap<Int, Int>, zones: HashMap<Int, HashMap<Int, HashSet<Point<Int, Int>>>>
    ) {
        var tmpP: TriPoint<Int, Int>
        for (xdx in markedImageMatrix) {
            for (tpt in xdx) {
                pointNumber[tpt.mark] = pointNumber.getOrDefault(tpt.mark, 0) + 1
            }
        }
        for (idx in markedImageMatrix.indices step 2) {
            for (jdx in markedImageMatrix[0].indices step 2) {
                square3GridFindConnection(idx, jdx, pointNumber = pointNumber, connections = zones)
            }
        }
    }


    private fun similar(img: BufferedImage) {
        var mark = 0
        var average = HashMap<Int, IntArray>()
        val counter = HashMap<Int, Int>()
        for (idx in 0 until markedImageMatrix.size) {
            for (jdx in 0 until markedImageMatrix[0].size) {
                nearby(idx, jdx, average, img, counter)
                var mindiff = MAX_DIFF
                for ((key, vlu) in average) {
                    val diff = ColorUtil.diff(vlu, img.getRgb(idx, jdx))
                    if (diff < mindiff) {
                        markedImageMatrix[idx][jdx].mark = key
                        mindiff = diff
                    }
                }
                if (mindiff >= MAX_DIFF) {// deal with (0,0)
                    markedImageMatrix[idx][jdx].mark = mark++
                }
            }
        }
    }

    /*
    get nearby rgb average of point x y
    return map(mark, averageColorArray)
     */
    private fun nearby(
        x: Int,
        y: Int,
        average: HashMap<Int, IntArray> = HashMap(),
        img: BufferedImage,
        counter: HashMap<Int, Int> = HashMap()
    ): HashMap<Int, IntArray> {
        average.clear()
        counter.clear()
        var mark = 0
        for (idx in -2..-1) {
            try {
                mark = markedImageMatrix[x][y + idx].mark
            } catch (e: ArrayIndexOutOfBoundsException) {
                continue
            }
            val rgb = ColorUtil.intToRgb(img.getRGB(x, y + idx))
            addRgb(average, mark, rgb, counter)
        }
        for (idx in 0..2) {
            try {
                mark = markedImageMatrix[x - 1][y + idx].mark
            } catch (e: Exception) {
                break
            }
            val rgb = ColorUtil.intToRgb(img.getRGB(x - 1, y + idx))
            addRgb(average, mark, rgb, counter)
        }
        for ((mark, vlu) in counter.entries) {
            val rgbArray = average[mark]!!
            val r = rgbArray[0] / vlu
            val g = rgbArray[0] / vlu
            val b = rgbArray[0] / vlu
            average[mark] = intArrayOf(r, g, b)
        }
        return average
    }

    private fun addRgb(
        average: HashMap<Int, IntArray>, mark: Int, rgb: IntArray, counter: HashMap<Int, Int>
    ) {
        val ave = average.getOrDefault(mark, intArrayOf(0, 0, 0))
        ave[0] += rgb[0]
        ave[1] += rgb[1]
        ave[2] += rgb[2]
        average[mark] = ave
        counter[mark] = counter.getOrDefault(mark, 0) + 1
    }

}
