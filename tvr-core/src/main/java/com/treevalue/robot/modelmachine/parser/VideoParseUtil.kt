package com.treevalue.robot.modelmachine.parser

import com.treevalue.robot.io.FileIOUtil
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import java.io.File
import java.util.*
import javax.imageio.ImageIO

object VideoParseUtil {

    val SEC_TO_US: Long = 1000000L
    val DENOMINATOR_TIME_DIVIDEDBY_FRAMERATE = 100000000L

    /**
    - Extract audio as mp3 clips from video files.
    - @param perTime for the length (us) of each audio clip. unit: us
    - @param intervalTime in us between each clip start time. unit: us
    - @param videoPath
    - @param  audioOutputPath eg: /usr/home/video/
    - all time unit :us
     */
    fun clipAudioSlicesAsMp3FromVideo(perTime: Long, intervalTime: Long, videoPath: String, audioOutputPath: String) {
        if (intervalTime < perTime || intervalTime <= 0 || perTime <= 0) {
            throw Exception("time set exception")
        }
        val grabber = FFmpegFrameGrabber(videoPath)
        try {
            grabber.start()
            val audioSampleRate = grabber.sampleRate
            val audioChannels = grabber.audioChannels

            var outputFileIndex = 0
            var timestamp = 0L
            val identifier = UUID.randomUUID().toString().replace("-", "")
            while (timestamp < grabber.lengthInTime) {
                val outputFilePath = "${audioOutputPath}${identifier}_${outputFileIndex++}.mp3"
                val recorder = FFmpegFrameRecorder(outputFilePath, audioChannels)
                recorder.sampleRate = audioSampleRate
                recorder.format = "mp3"
                recorder.audioCodec = avcodec.AV_CODEC_ID_MP3
                recorder.start()

                var recordedTime = 0.0
                while (recordedTime < perTime) {
                    val frame: Frame = grabber.grabFrame() ?: break
                    if (frame.image != null) {
                        continue // Skip non-audio frames
                    }
                    recorder.record(frame)
                    recordedTime += DENOMINATOR_TIME_DIVIDEDBY_FRAMERATE / frame.sampleRate
                }
                recorder.stop()
                recorder.release()
                timestamp += intervalTime
            }
        } finally {
            grabber.stop()
            grabber.release()
        }

    }

    @Throws(Exception::class)
    fun clipVideoFromStartToEnd(inputPath: String?, outputPath: String?, startSecond: Double, endSecond: Double) {
        val grabber = FFmpegFrameGrabber(inputPath)
        grabber.start()
        val recorder = FFmpegFrameRecorder(outputPath, grabber.imageWidth, grabber.imageHeight, grabber.audioChannels)
        recorder.format = grabber.format
        recorder.frameRate = grabber.frameRate
        recorder.sampleRate = grabber.sampleRate
        recorder.start()
        grabber.timestamp = (startSecond * 1000000).toLong()
        var frame: Frame?
        while (grabber.grabFrame()
                .also { frame = it } != null && grabber.timestamp < (endSecond * 1000000).toLong()
        ) {
            recorder.record(frame)
        }
        recorder.stop()
        grabber.stop()
    }


    fun removeAudioFromVideo(videoWithAudioPath: String, nonAudioVideooutputPath: String) {
        val name = File(videoWithAudioPath).name
        if (name.isBlank() || !File(nonAudioVideooutputPath).isDirectory) {
            throw Exception("file path exception")
        }
        var outPath = ""
        if (nonAudioVideooutputPath.endsWith(File.separator)) {
            outPath = "${nonAudioVideooutputPath}${name}"
        } else {
            outPath = "${nonAudioVideooutputPath}${File.separator}${name}"
        }
        try {
            FFmpegFrameGrabber(videoWithAudioPath).use { grabber ->
                grabber.start()
                FFmpegFrameRecorder(outPath, grabber.imageWidth, grabber.imageHeight, 0).use { recorder ->
                    recorder.format = grabber.format
                    recorder.frameRate = grabber.frameRate
                    recorder.videoBitrate = grabber.videoBitrate
                    recorder.start()
                    var frame: Frame?
                    while (grabber.grabFrame(false, true, true, false).also { frame = it } != null) {
                        recorder.record(frame)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
    Extract images from a video by interval
    @param videoPath The path of the video file
    @param intervalTimeByUs The interval at which the image is extracted, in microseconds (us)
    @param imageOutputPath The directory where the image is output
     */
    fun extractImageByIntervalFromVideo(
        videoPath: String,
        intervalTimeByUs: Long,
        imageType: String = "jpg",
        imageOutputPath: String
    ) {
        val grabber = FFmpegFrameGrabber(videoPath)
        val converter = Java2DFrameConverter()
        val name = File(videoPath).name
        try {
            grabber.start()

            var frameNumber: Long = 0
            while (true) {
                val frame = grabber.grabImage() ?: break

                val timestamp = grabber.timestamp
                if (timestamp >= frameNumber * intervalTimeByUs) {
                    val bufferedImage = converter.convert(frame)
                    val outputfile = File("${FileIOUtil.getDefaultDirectory(imageOutputPath)}${name}_${frameNumber}.${imageType}")
                    ImageIO.write(bufferedImage, imageType, outputfile)
                    frameNumber++
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            grabber.stop()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val inputVideo =
                "D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\resources\\test\\video\\KeukenhofTulipGarden.mp4"
            val outputVideo =
                "D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\resources\\test\\video\\KeukenhofTulipGarden_small.mp4"
            val start = 111.00
            val end = 127.00
            clipVideoFromStartToEnd(inputVideo, outputVideo, start, end)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

