package com.treevalue.robot.memeryriver.physicallayer

import com.treevalue.robot.data.CircularBuffer

// raw audio 0-> left and 1-> right data
class AudioCachePool(capacity: Int = 21) : CircularBuffer<Array<Array<Short>>>(capacity)
