package com.treevalue.robot.memeryriver.logiclayer

import ai.djl.ndarray.NDArray
import com.treevalue.robot.anno.TopApi
import com.treevalue.robot.data.CircularBuffer
import com.treevalue.robot.data.Point
import com.treevalue.robot.data.Singleton
import com.treevalue.robot.memeryriver.physicallayer.AudioCachePool
import com.treevalue.robot.memeryriver.physicallayer.RawAudio
import com.treevalue.robot.memeryriver.physicallayer.RawImageClassify
import com.treevalue.robot.memeryriver.physicallayer.VisualCachePool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class StartPoint {
    private val visualCachePool: VisualCachePool = Singleton.getVisualCachePool()
    private val audioCachePool: AudioCachePool = Singleton.getAudioCachePool()
    private val visualTensorCachePool: CircularBuffer<NDArray> = Singleton.getVisualTensorCachePool()
    private var visualMarksSet: CircularBuffer<HashSet<Int>> = Singleton.getVisualMarkCachePool()
    private var visualBoundsCachePool: CircularBuffer<HashMap<Int, HashSet<Point<Int, Int>>>> =
        Singleton.getVisualBoundsCachePool()
    private val audioTensorCachePool: CircularBuffer<NDArray> = Singleton.getAudioTensorCachePool()
    private var audioMarksSet: CircularBuffer<HashSet<Int>> = Singleton.getAudioMarkCachePool()
    private val visualMarker: RawImageClassify = RawImageClassify()
    private val audioMarker: RawAudio = RawAudio()
    private val lock = ReentrantLock()

    fun pop(): Array<Any> {
        try {
            lock.withLock {
                return arrayOf(popAudioVisualMarksSet(), popRawData(), visualBoundsCachePool.pop()!!)
            }
        } finally {
            lock.unlock()
        }
    }


    private fun popAudioVisualMarksSet(): Array<HashSet<Int>?> {
        return arrayOf(visualMarksSet.pop(), audioMarksSet.pop())
    }

    private fun popRawData(): Array<NDArray?> {
        var visual = visualTensorCachePool.pop()
        var count = 0
        while (visual == null && count < 4) {
            Thread.sleep(50)
            visual = visualTensorCachePool.pop()
            count++
        }
        /*
        visual (width, height, (rgb, mark)
        audio (number, freq, (magnitude, mark))
         */
        return arrayOf(visual, audioTensorCachePool.pop())
    }

    @TopApi
    fun start() {
        rawDataToTensor()
    }

    private fun rawDataToTensor() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (!visualCachePool.isEmpty) {
                    val img = visualCachePool.pop()
                    if (img != null) visualMarker.resetImage(img)
                    val aud = audioCachePool.pop()
                    if (aud != null) audioMarker.audioToPeakValleyMatrix(aud[0].map { it.toDouble() }.toDoubleArray())
                    try {
                        lock.withLock {
                            audioTensorCachePool.push(audioMarker.getPeakValleyAudioTensor())
                            audioMarksSet.push(audioMarker.getMarkSet())
                            visualTensorCachePool.push(visualMarker.getMarkedImageTensor())
                            visualMarksSet.push(visualMarker.markSet)
                            visualBoundsCachePool.push(visualMarker.getBounds())
                        }
                    } finally {
                        lock.unlock()
                    }

                } else {
                    delay(50)
                }
            }
        }
    }
}
