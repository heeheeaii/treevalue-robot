package com.treevalue.robot.modelmachine.parser

import be.tarsos.dsp.io.jvm.AudioDispatcherFactory
import com.treevalue.robot.global.GlobalData
import com.treevalue.robot.io.FileIOUtil
import com.treevalue.robot.modelmachine.parser.audioProcessor.HarmonicProcessor
import com.treevalue.robot.modelmachine.recognizer.audio.constant.AudioData
import com.treevalue.robot.modelmachine.recognizer.audio.filter.AudioPointFilter
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FFmpegFrameRecorder
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.UnsupportedAudioFileException

object AudioParseUtil {

    fun clipAudioFromTo(inputPath: String, outputPath: String, startTime: Double, endTime: Double) {

        val grabber = FFmpegFrameGrabber(inputPath)
        try {
            grabber.start()
            val recorder = FFmpegFrameRecorder(outputPath, grabber.audioChannels)
            recorder.sampleRate = grabber.sampleRate
            recorder.audioCodec = grabber.audioCodec
            recorder.format = grabber.format
            recorder.start()

            var frame = grabber.grab()
            var timestamp: Double

            while (frame != null) {
                timestamp = grabber.timestamp.toDouble()
                if (timestamp > endTime) break
                if (timestamp >= startTime) {
                    if (timestamp <= endTime) {
                        recorder.record(frame)
                    } else {
                        break
                    }
                }
                frame = grabber.grab()
            }
            recorder.stop()
            recorder.release()
        } finally {
            grabber.stop()
            grabber.release()
        }
    }

    private fun extraCodeForResult(
        durationBySec: Double = 0.08,
        audioFile: File
    ): ArrayList<FloatArray> {
        if (!audioFile.isFile) throw Exception("audioPath don't direct a audio file")
        val format = AudioSystem.getAudioFileFormat(audioFile).format
        val bufferSize = (format.sampleRate * durationBySec).toInt()
        val fftWindowSizeRate = 0.5
        val dispatcher =
            AudioDispatcherFactory.fromFile(audioFile, bufferSize, (bufferSize * fftWindowSizeRate).toInt())
        val processor = HarmonicProcessor(bufferSize)
        dispatcher.addAudioProcessor(processor)
        dispatcher.run()
        return processor.result
    }

    private fun extractedForOutputName(outputFileName: String, audioFile: File): String {
        if ("".equals(outputFileName)) {
            return audioFile.name.replace(".", "")
        }
        return outputFileName
    }

    private fun extractedForOutputDirectory(outputDirectory: String, audioFile: File): String {
        if (GlobalData.CUR_DIRECTORY.equals(outputDirectory)) {
            return "${FileIOUtil.getDefaultDirectory(audioFile.parent)}moment${GlobalData.SEPARATOR}"
        }
        return outputDirectory
    }

    // widow is half of buffer size so it's
    fun getHarmonicAllInfo(
        audioPath: String,
        durationBySec: Double = 0.08,
        header: String = "${AudioData.HEADER_SEQ}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_FREQUENCY}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_AMPLITUDE}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_ENERGY}${AudioData.HEADER_ENDL}",
        outputDir: String = ".${File.separator}",
        outputFileName: String = "",
    ) {
        try {
            val audioFile = File(audioPath)
            var outputDirectory = extractedForOutputDirectory(outputDir, audioFile)
            var outputName = extractedForOutputName(outputFileName, audioFile)
            val result = extraCodeForResult(durationBySec, audioFile)

            var seq = 0.0F
            var writer: FileWriter = createWriterAndWriteHeader(outputDirectory, outputName, seq.toInt(), header)
            with(writer) {
                for (record in result) {
                    if (seq != record[0]) {
                        writer.flush()
                        writer.close()
                        seq = record[0]
                        writer = createWriterAndWriteHeader(outputDirectory, outputName, seq.toInt(), header)
                    }
                    writer.write("${record[0].toInt()}\t${record[1]}\t${record[2]}\t${record[3]}\n")
                }
                writer.flush()
            }
        } catch (e: UnsupportedAudioFileException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getHarmonicInfoLargeThanZero(
        audioPath: String,
        durationBySec: Double = 0.08,
        header: String = "${AudioData.HEADER_SEQ}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_FREQUENCY}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_AMPLITUDE}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_ENERGY}${AudioData.HEADER_ENDL}",
        outputDir: String = ".${File.separator}",
        outputFileName: String = "",
    ) {
        try {
            val audioFile = File(audioPath)
            var outputDirectory = extractedForOutputDirectory(outputDir, audioFile)
            var outputName = extractedForOutputName(outputFileName, audioFile)
            val result = extraCodeForResult(durationBySec, audioFile)

            var seq = 0.0F
            var writer: FileWriter = createWriterAndWriteHeader(outputDirectory, outputName, seq.toInt(), header)
            with(writer) {
                for (record in result) {
                    if (AudioPointFilter.smallThan(record[2], 0f)) continue
                    if (seq != record[0]) {
                        writer.flush()
                        writer.close()
                        seq = record[0]
                        writer = createWriterAndWriteHeader(outputDirectory, outputName, seq.toInt(), header)
                    }
                    writer.write("${record[0].toInt()}\t${record[1]}\t${record[2]}\t${record[3]}\n")
                }
                writer.flush()
            }
        } catch (e: UnsupportedAudioFileException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun createWriterAndWriteHeader(
        outputDirectory: String, outputName: String, seq: Int, header: String
    ): FileWriter {
        val filePath = "${outputDirectory}_${seq}_${outputName.replace(".", "")}.tsv"
        val file = File(filePath)
        val parentDir = file.parentFile
        if (!parentDir.exists()) {
            val dirsCreated = parentDir.mkdirs()
            if (!dirsCreated) {
                throw IOException("Failed to create parent directories: ${parentDir.path}")
            }
        }
        if (!file.exists()) {
            val fileCreated = file.createNewFile()
            if (!fileCreated) {
                throw IOException("Failed to create new file: $filePath")
            }
        }
        val writer = FileWriter(file, StandardCharsets.UTF_8, true)

        writer.write(header)
        return writer
    }

    @JvmStatic
    fun main(args: Array<String>) {
        getHarmonicAllInfo("D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\resources\\test\\audio\\fft\\forest_in_night_small.wav")
    }
}
