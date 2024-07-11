/**
The hardships you have endured are your ultimate bulwark.
 */
package com.treevalue.robot.memeryriver.gradientlayer

import ai.djl.ndarray.NDArray
import com.treevalue.robot.data.Point
import com.treevalue.robot.data.constant.VisualConstant
import com.treevalue.robot.memeryriver.MemoryRiver
import com.treevalue.robot.memeryriver.Relation
import com.treevalue.robot.memeryriver.TimeSlice
import com.treevalue.robot.memeryriver.logiclayer.StartPoint

class ShallowLayer {
    var MARK: Long = Long.MIN_VALUE
    lateinit var visualCanvas: VisualCanvas

    private val rows: Rows = Rows()

    //    10d
    private var preTimeSlice: TimeSlice? = null
    private val start: StartPoint = StartPoint()

    init {
        try {
            preTimeSlice = MemoryRiver.pre()
        } catch (e: NoSuchElementException) {
        }
    }

    //    todo : test code
    fun preliminaryVectorization() {
        val (var1, var2, var3) = start.pop()
        val marks = var1 as Array<Int?>
        val tensors = var2 as Array<NDArray?>
        val bounds = var3 as HashMap<Int, HashSet<Point<Int, Int>>>
        var finish = 0
        for (tsr in tensors) {
            if (tsr == null) finish++
        }
        if (finish == tensors.size) return
        for (tsr in tensors) {
            if (tsr == null) continue
            if (tsr.shape.shape.last() != 2L) throw IllegalArgumentException("tensors must is (space dimension) + (basicAttribute, mark)")
        }
        val tms: TimeSlice = TimeSlice()
        tms.raw = tensors
        val relations = getRelation(marks, tensors)
        tms.connects = relations
        tms.time = System.nanoTime()
        tms.bounds = bounds
        assimilateAndGo(preTimeSlice, tms, bounds)
        preTimeSlice?.let { MemoryRiver.push(it) }
        preTimeSlice = tms
    }

    private fun assimilateAndGo(
        pre: TimeSlice?, cur: TimeSlice, bounds: HashMap<Int, HashSet<Point<Int, Int>>>
    ) {
        pre ?: return
        val size = VisualConstant.visualMatrixWidthHeight ?: return
        if (visualCanvas == null || visualCanvas.data.size != size[0] || visualCanvas.data[0].size != size[1]) {
            visualCanvas = VisualCanvas(size[0], size[1])
        }
        visualCanvas.background(pre.raw[1])
        rows.setBounds(bounds)
        var continues: Int?
        var inners: Array<Point<Int, Int>>
        for (mark in bounds.keys) {
            inners = rows.getInners(mark)
            continues = visualCanvas.predict(inners)
            continues?.takeIf {
                continues != mark
            }.let {
                cur.redraw(mark, continues!!, rows.curBoundsStatics())
            }
        }

    }

    private fun getRelation(marks: Array<Int?>, tensors: Array<NDArray?>): Array<Relation> {
        val tmp = start.pop()
        val markSet = tmp[0] as Array<HashSet<Int>?>
        val rawData = tmp[1] as Array<NDArray?>
        val rawRelation = Array(rawData.size) { Relation() }
        for (idx in rawRelation.indices) {
            if (markSet[idx] == null) continue
            rawRelation[idx].from = markSet[idx]!!.map { it.toLong() }.toHashSet()
            rawRelation[idx].conns = Array(markSet[idx]!!.size) { Array(markSet[idx]!!.size) { true } }
        }
        return rawRelation
    }

    // todo
    fun deepVectorization() {


    }
}
