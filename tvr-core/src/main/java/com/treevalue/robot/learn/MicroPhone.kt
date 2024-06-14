package com.treevalue.robot.learn

import com.treevalue.robot.data.Singleton
import com.treevalue.robot.pool.ThreadPool
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class MicroPhone {
    private var listenExecuted = AtomicBoolean(false)
    private val format = Singleton.getAudioFormat()
    private val line = Singleton.getAudioTargetLine()

    fun recordToMemory(durationInMillis: Long): ByteArray {
        line.open(format)
        line.start()

        val out = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < durationInMillis) {
            val count = line.read(buffer, 0, buffer.size)
            if (count > 0) {
                out.write(buffer, 0, count)
            }
        }

        line.stop()
        line.close()

        return out.toByteArray()
    }

    fun recordToFile(durationInMillis: Long, filePath: String) {
        line.open(format)
        line.start()

        val audioStream = AudioInputStream(line)
        val file = File(filePath)
        val writerThread = Thread {
            AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, file)
        }

        writerThread.start()
        Thread.sleep(durationInMillis)

        line.stop()
        line.close()
        audioStream.close()
    }

    fun listen() {
        if (!listenExecuted.get()) {
            listenExecuted.set(true);
            val cache = Singleton.getAudioCachePool();
            val task = Runnable {
                val buffer = ByteArray(8192);
                line.open(format);
                line.start();
                while (true) {
                    val bytesRead = line.read(buffer, 0, buffer.size);
                    if (bytesRead > 0) {
                        val leftChannel = Array<Short>(2048) { 0 };
                        val rightChannel = Array<Short>(2048) { 0 };
                        var i = 0;
                        while (i < bytesRead) {
                            val leftSample = (buffer[i].toInt() and 0xff or (buffer[i + 1].toInt() shl 8)).toShort();
                            val rightSample =
                                (buffer[i + 2].toInt() and 0xff or (buffer[i + 3].toInt() shl 8)).toShort();
                            leftChannel[i / 4] = leftSample;
                            rightChannel[i / 4 + 1] = rightSample;
                            i += 4;
                        }
                        val stereoSamples = arrayOf(leftChannel, rightChannel);
                        cache.push(stereoSamples);
                    }
                }
            };
            ThreadPool.getScheduler().scheduleAtFixedRate(task, 0, 47619, TimeUnit.MICROSECONDS);// 21 times
        }
    }
}
