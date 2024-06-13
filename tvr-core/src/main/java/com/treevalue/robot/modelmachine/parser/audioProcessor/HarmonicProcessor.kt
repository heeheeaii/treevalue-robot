package com.treevalue.robot.modelmachine.parser.audioProcessor

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.util.fft.FFT

class HarmonicProcessor(
    private val bufferSize: Int,
    val fftSize: Int = 1024
) : AudioProcessor {
    private val fft = FFT(fftSize)

    /*
    FFT Size	Frequency Resolution
    1024     43.05 Hz
    2048	21.53 Hz
    4096	10.77 Hz
     */
    private val amplitudes = FloatArray(bufferSize / 2)
    private var seq = 0.0f
    val result = ArrayList<FloatArray>()
    override fun process(audioEvent: AudioEvent?): Boolean {
        if (audioEvent == null) return false

        val audioBuffer = audioEvent.floatBuffer
        fft.forwardTransform(audioBuffer)
//        fft call algorithm, fftSize effect performance
        fft.modulus(audioBuffer, amplitudes)

        for (idx in amplitudes.indices) {
            val frequency = idx * audioEvent.sampleRate / amplitudes.size
            val amplitude = amplitudes[idx]
            val dB = 20 * Math.log10(amplitude.toDouble()).toFloat()
            val energy = (amplitude * amplitude)
            result.add(floatArrayOf(seq, frequency, dB, energy))
        }
        seq += 1
        return true
    }

    override fun processingFinished() {
    }
}
