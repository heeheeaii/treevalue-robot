package com.treevalue.robot.memeryriver.physicallayer

import ai.djl.ndarray.NDArray
import ai.djl.ndarray.index.NDIndex
import ai.djl.ndarray.types.Shape
import be.tarsos.dsp.filters.BandPass
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory
import be.tarsos.dsp.writer.WriterProcessor
import com.treevalue.robot.pool.ThreadPool
import com.treevalue.robot.tensor.transfer.TensorManager
import com.treevalue.robot.tensor.transfer.stft.STFT
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import javax.sound.sampled.UnsupportedAudioFileException

/*
todo: do relative similar visual whether do wait after some time
 */

class RawAudio {
    private var marks = 0
    private lateinit var markedAudio: Array<FloatArray>
    private var markSet: HashSet<Int> = HashSet()

    fun getMarkSet(): HashSet<Int> {
        return markSet
    }

    fun getMarks(): Int {
        return marks
    }

    // chanel(1 or 2), freq, (mag, mark)
    fun getPeakValleyAudioTensor(): NDArray {
        require(markedAudio != null)
        val manager = TensorManager.getManager()
        val numRows = markedAudio.size
        val numCols = markedAudio[0].size / 2
        marks = numCols
        val tensor = manager.create(Shape(numRows.toLong(), numCols.toLong(), 2))

        markSet.clear()
        for (i in markedAudio.indices) {
            for (j in 0 until markedAudio[i].size step 2) {
                tensor.set(NDIndex(i.toLong(), j.toLong(), 0), markedAudio[i][j])
                tensor.set(NDIndex(i.toLong(), j.toLong(), 1), markedAudio[i][j + 1])
                markSet.add(markedAudio[i][j + 1].toInt())
            }
        }
        return tensor
    }

    fun resetAudio(channel: DoubleArray) {
        audioToPeakValleyMatrix(channel)
    }

    fun audioToPeakValleyMatrix(channel: DoubleArray) {
        val stftFloatMatrix = STFT.stftToFloatMatrix(channel)
        markedAudio = stftFloatMatrix.map {
            peakValleyMark(it)
        }.toTypedArray()
    }

    /*
    val array = floatArrayOf(1f, 2f, 3f, 2f, 1f, 2f, 3f, 4f, 3f, 2f, 1f, 2f)
    val result = RawAudio().peakValleyMark(array)
    println(result.joinToString(", "))
    1.0, 0.0, 2.0, 0.0, 3.0, 0.0, 2.0, 1.0, 1.0, 1.0, 2.0, 1.0, 3.0, 1.0, 4.0, 1.0, 3.0, 1.0, 2.0, 2.0, 1.0, 2.0, 2.0, 3.0
     */
    private fun peakValleyMark(array: FloatArray): FloatArray {
        val rst = FloatArray(array.size * 2) { if (it % 2 == 0) array[it / 2] else 0f }

        if (array.size <= 3) {
            if (array.size == 3 && array[1] < array[0] && array[1] < array[2]) {
                rst[5] = 1f
            }
            return rst
        }

        var slider = FloatArray(3) { Float.MAX_VALUE }
        val size = array.size
        var pre = 0f
        rst[1] = pre
        slider[0] = array[0]
        var pta = 1
        var ptb = 1

        while (ptb < size) {
            if (ptb + 1 < size) {
                slider[1] = array[ptb++]
                slider[2] = array[ptb++]
                rst[2 * pta + 1] =
                    if ((slider[0] >= slider[1] && slider[2] > slider[1]) || (ptb - 2 >= 0 && array[ptb] > array[ptb - 1] && array[ptb - 2] >= array[ptb - 1])) {
                        ++pre
                    } else {
                        pre
                    }
                rst[2 * ++pta + 1] = pre
                slider[0] = slider[2]
                ++pta
            } else if (ptb < size) {
                rst[2 * ptb + 1] = if (array[ptb] > array[ptb - 1] && array[ptb - 2] >= array[ptb - 1]) {
                    ++pre
                } else {
                    pre
                }
                ptb++
            }
        }
        return rst
    }

    @Throws(IOException::class, UnsupportedAudioFileException::class)
    fun boundPass(inputWav: String, outputWav: String) {
        val sampleRate = 44100f
        val bufferSize = 1024
        val overlap = 0
        val dispatcher = AudioDispatcherFactory.fromFile(File(inputWav), bufferSize, overlap)
        val centerFrequency = (20000 + 10) / 2.0f
        val bandwidth = (20000 - 10).toFloat()
        val bandPass = BandPass(centerFrequency, bandwidth, sampleRate)

        dispatcher.addAudioProcessor(bandPass)

        val outputRAF = RandomAccessFile(outputWav, "rw")
        dispatcher.addAudioProcessor(WriterProcessor(dispatcher.format, outputRAF))
        val dispatcherThread = Thread(dispatcher, "Audio Dispatcher")
        ThreadPool.transformThreadPool.submit(dispatcher)
    }
}
