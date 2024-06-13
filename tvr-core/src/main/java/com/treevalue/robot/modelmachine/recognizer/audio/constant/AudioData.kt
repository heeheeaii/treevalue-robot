package com.treevalue.robot.modelmachine.recognizer.audio.constant
object AudioData {
    val HEADER_SEPARATOR = "\t"
    val HEADER_FREQUENCY = "Frequency/Hz"
    val HEADER_SEQ = "Seq"
    val HEADER_AMPLITUDE = "Amplitude/dB"
    val HEADER_ENERGY = "Energy"
    val HEADER_ENDL = "\n"
}

fun main(){
    print(
        "${AudioData.HEADER_SEQ}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_FREQUENCY}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_AMPLITUDE}${AudioData.HEADER_SEPARATOR}${AudioData.HEADER_ENERGY}${AudioData.HEADER_ENDL}"
    )
}
