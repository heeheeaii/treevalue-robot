package com.treevalue.robot.pool

import com.treevalue.robot.data.CircularBuffer

class AudioCachePool(capacity: Int = 21) : CircularBuffer<Array<Array<Short>>>(capacity)
