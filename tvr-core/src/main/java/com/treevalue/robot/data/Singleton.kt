package com.treevalue.robot.data

import com.treevalue.robot.pool.AudioCachePool
import com.treevalue.robot.pool.VisualCachePool
import java.awt.Robot
import java.util.concurrent.locks.ReentrantLock
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.concurrent.withLock

object Singleton {

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
}
