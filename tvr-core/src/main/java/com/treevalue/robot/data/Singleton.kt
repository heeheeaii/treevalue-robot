package com.treevalue.robot.data

import ai.djl.ndarray.NDArray
import com.treevalue.robot.memeryriver.physicallayer.AudioCachePool
import com.treevalue.robot.memeryriver.physicallayer.VisualCachePool
import java.awt.Robot
import java.util.concurrent.locks.ReentrantLock
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.concurrent.withLock

object Singleton {
    private val BASIC_SIZE: Int = 21

    @Volatile
    private var robot: Robot? = null

    @Volatile
    private var format: AudioFormat? = null

    @Volatile
    private var info: DataLine.Info? = null

    private val robotLock = ReentrantLock()
    private val formatLock = ReentrantLock()
    private val infoLock = ReentrantLock()

    private val visualCachePool = VisualCachePool()
    private val audioCachePool = AudioCachePool()
    private val visualTensorCachePool: CircularBuffer<NDArray> = CircularBuffer(BASIC_SIZE)
    private var visualMarksNumber: CircularBuffer<Int> = CircularBuffer(BASIC_SIZE)
    private val audioTensorCachePool: CircularBuffer<NDArray> = CircularBuffer(BASIC_SIZE)
    private val audioMarkCachePool: CircularBuffer<HashSet<Int>> = CircularBuffer(BASIC_SIZE)

    //    bounds: < mark, bounds of mark self>
    private val visualBoundCachePool: CircularBuffer<HashMap<Int, HashSet<Point<Int, Int>>>> =
        CircularBuffer(BASIC_SIZE)
    private val visualConsCachePool: CircularBuffer<HashMap<Int, HashSet<Int>>> = CircularBuffer(BASIC_SIZE)
    private val visualMarkCachePool: CircularBuffer<HashSet<Int>> = CircularBuffer(BASIC_SIZE)
    private var audioMarksNumber: CircularBuffer<Int> = CircularBuffer(BASIC_SIZE)

    fun getAudioMarkCachePool(): CircularBuffer<HashSet<Int>> {
        return visualMarkCachePool
    }

    fun getVisualMarkCachePool(): CircularBuffer<HashSet<Int>> {
        return visualMarkCachePool
    }

    fun getVisualConsCachePool(): CircularBuffer<HashMap<Int, HashSet<Int>>> {
        return visualConsCachePool
    }

    fun getVisualMarksNumber(): CircularBuffer<Int> {
        return visualMarksNumber
    }

    fun getAudioMarksNumber(): CircularBuffer<Int> {
        return audioMarksNumber
    }

    fun getRobot(): Robot {
        return robot ?: robotLock.withLock {
            robot ?: Robot().also { robot = it }
        }
    }

    fun getAudioFormat(): AudioFormat {
        return format ?: formatLock.withLock {
            format ?: AudioFormat(44100.0f, 16, 2, true, true).also { format = it }
        }
    }

    fun getAudioTargetLine(): TargetDataLine {
        val localFormat = getAudioFormat() // Ensure format is initialized
        return info?.let {
            AudioSystem.getLine(it) as TargetDataLine
        } ?: infoLock.withLock {
            info?.let {
                AudioSystem.getLine(it) as TargetDataLine
            } ?: DataLine.Info(TargetDataLine::class.java, localFormat).also {
                info = it
            } as TargetDataLine
        }
    }

    fun getVisualCachePool(): VisualCachePool {
        return visualCachePool
    }


    fun getAudioCachePool(): AudioCachePool {
        return audioCachePool
    }

    fun getAudioTensorCachePool(): CircularBuffer<NDArray> {
        return audioTensorCachePool
    }

    fun getVisualTensorCachePool(): CircularBuffer<NDArray> {
        return visualTensorCachePool
    }

    fun getVisualBoundsCachePool(): CircularBuffer<HashMap<Int, HashSet<Point<Int, Int>>>> {
        return visualBoundCachePool
    }
}
