package com.treevalue.robot.pool

import com.treevalue.robot.data.CircularBuffer
import java.awt.image.BufferedImage

class VisualCachePool(capacity: Int = 21) : CircularBuffer<BufferedImage>(capacity)
