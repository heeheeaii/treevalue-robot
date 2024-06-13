package com.treevalue.robot.modelmachine.parser.config

import org.bytedeco.javacpp.Loader
import org.bytedeco.opencv.opencv_java
import org.springframework.context.annotation.Configuration

@Configuration
open class JavacvConfig {
    var opencvName = ""
    init {
         opencvName = Loader.load(opencv_java::class.java)
    }
}
