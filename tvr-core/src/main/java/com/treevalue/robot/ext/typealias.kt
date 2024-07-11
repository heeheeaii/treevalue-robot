package com.treevalue.robot.ext

import com.treevalue.robot.data.DynamicArray
import com.treevalue.robot.data.Point

// mark, [timestampData1,timestampData2... ]
typealias ThingData = HashMap<Int, DynamicArray<HashSet<Point<Int, Int>>>>
