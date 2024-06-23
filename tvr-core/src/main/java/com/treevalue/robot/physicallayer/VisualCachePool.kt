package com.treevalue.robot.physicallayer

import com.treevalue.robot.data.CircularBuffer
import java.awt.image.BufferedImage

// raw screen shortcut image
class VisualCachePool(capacity: Int = 21) : CircularBuffer<BufferedImage>(capacity)
