package com.treevalue.robot.io

import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString


object FileIOUtil {
    fun getDefaultDirectory(path: String): String {
        //    use file separator in current os as tail
        if (File(path).isDirectory) {
            if (!path.endsWith(File.separator)) {
                return "${path}${File.separator}"
            } else {
                return path
            }
        }
        return "./"
    }

    //could: test/test.txt, test.html, /test/test.html, classpath: /test.txt, classpath:test.html
    fun getSourceAbsolutePath(filePathInResource: String = ""): String? {
        var path: String = filePathInResource
        if (path.startsWith("classpath:")) {
            path = path.substring(10).trim()
        }
        if (path.startsWith("/")) {
            path = path.substring(1)
        }
        val classLoader = Thread.currentThread().contextClassLoader
        val resourceUrl: URL? = classLoader.getResource(path)
        return resourceUrl?.toURI()?.let { Paths.get(it).toAbsolutePath().toString() }
    }

    fun createIfNotExist(filePath: String): String {
        val path: Path = Paths.get(filePath)
        if (!Files.exists(path.parent)) {
            Files.createDirectories(path.parent)
        }
        if (!Files.exists(path)) {
            Files.createFile(path)
        }
        return filePath
    }

    fun createIfNotExist(filePath: Path): String {
        return createIfNotExist(filePath.absolutePathString())
    }
}
