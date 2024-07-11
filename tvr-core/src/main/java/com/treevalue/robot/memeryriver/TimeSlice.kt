package com.treevalue.robot.memeryriver

import ai.djl.ndarray.NDArray
import ai.djl.ndarray.index.NDIndex
import com.treevalue.robot.data.Point

class TimeSlice {


    //    visual (width, height, (rgbInt,mark)), audio(slices, freq, (magnitude, mark))
    lateinit var raw: Array<NDArray?>

    //    record timestamp
    var time: Long = Long.MIN_VALUE

    lateinit var connects: Array<Relation>

    //    bounds: < mark, bounds of mark self>
    lateinit var bounds: HashMap<Int, HashSet<Point<Int, Int>>>

    fun redraw(mark: Int, continues: Int, curBoundsStatics: LinkedHashMap<Int, ArrayList<Int>>) {
        bounds[mark] ?: return
        val sealPoints: HashSet<Point<Int, Int>> = bounds[mark]!!
        bounds[continues] = sealPoints
        bounds.remove(mark)
        raw.forEachIndexed { index, ndArray ->
            if (ndArray != null) {
                for ((xdx, ys) in curBoundsStatics) {
                    for (ydx in ys.indices step 2) {
                        if (ydx + 1 < ys.size) {
                            for (idx in ys[ydx]..ys[ydx + 1]) {
                                val idx = NDIndex("$xdx,$idx,0")
                                ndArray.set(idx, continues)
                            }
                        }
                    }
                }
            }
        }
    }
}
