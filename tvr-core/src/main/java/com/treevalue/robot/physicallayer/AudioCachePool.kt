package com.treevalue.robot.physicallayer

import com.treevalue.robot.data.CircularBuffer

// row audio left and right data
class AudioCachePool(capacity: Int = 21) : CircularBuffer<Array<Array<Short>>>(capacity)
