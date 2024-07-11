package com.treevalue.robot.memeryriver.gradientlayer

import com.treevalue.robot.data.DynamicArray

/*
express a thing inner relationship
 */
class Header {
    // as a setup point, as a center of reference system
    val center: Int = Int.MIN_VALUE

    /*
       thing from some and make it b
       [
       <[<mark1, position1 from respective header>,<m2, p2>],
       [<mark1, position1 from respective header>,<m2, p2>],
       >] ,
       [],
       []
       ]
   */
    lateinit var relation: DynamicArray<Pair<DynamicArray<Pair<Int, Int>>, DynamicArray<Pair<Int, Int>>>>

    fun expandAll(): HashSet<Pair<Int, Int>> {
        val rst = HashSet<Pair<Int, Int>>()
        for (itm in relation) {
            for (itmFrom in itm.first) {
                rst.add(itmFrom)
            }
            for (itmTo in itm.second) {
                rst.add(itmTo)
            }
        }
        return rst
    }
}
