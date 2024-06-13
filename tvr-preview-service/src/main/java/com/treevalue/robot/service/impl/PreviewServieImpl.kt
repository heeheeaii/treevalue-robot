package com.treevalue.robot.service.impl

import com.treevalue.robot.record.PreviewString
import com.treevalue.robot.data.RetResourceOrPreview
import com.treevalue.robot.data.TextCodingType
import com.treevalue.robot.record.ResourceExtension
import com.treevalue.robot.service.PreviewServie
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import kotlin.collections.HashMap
import kotlin.random.Random
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class PreviewServieImpl : PreviewServie {

    override fun getPreview(waitThings: HashMap<HashMap<String, String>, ByteArray>): ArrayList<RetResourceOrPreview> {
        val list = ArrayList<RetResourceOrPreview>()
        waitThings.forEach {
            when (it.key[PreviewString.NAME]) {
                ResourceExtension.TXT.ext -> {
                    it.value?.let { it1 ->
                        val txtByte = it.key[PreviewString.BYTE_TYPE]?.let { it2 -> TxtGeneratePreview(it2, it1) }
                        list.add(RetResourceOrPreview(it.key[PreviewString.NAME], txtByte))
                    }
                }

                ResourceExtension.PNG.ext -> {
                    it.value?.let { it1 ->
                        val pngBytes =
                            it.key[PreviewString.BYTE_TYPE]?.let { it2 -> pngJpegGeneratePreview(it.key, it1) }
                        list.add(RetResourceOrPreview(it.key[PreviewString.NAME], pngBytes))
                    }
                }
            }
        }
        return list

    }

    private fun TxtGeneratePreview(type: String, text: ByteArray): ByteArray {
        //v2.0 check us npl, and extra preview
        val sb = StringBuffer()
        when (type) {
            TextCodingType.UTF8.type -> {
                val str = String(text, StandardCharsets.UTF_8)
                for (i in 0..2) {
                    var beg = (Random.nextDouble() * str.length).toInt()
                    var finish = beg + (0.05 * str.length).toInt()
                    finish = if (finish < str.length) finish else str.length
                    sb.append(str.substring(beg, finish))
                }
                return sb.toString().toByteArray(Charsets.UTF_8)
            }
        }
        return sb.toString().toByteArray(Charsets.UTF_8)
    }

    fun pngJpegGeneratePreview(map: HashMap<String, String>, bytes: ByteArray): ByteArray {
        val originalImage = ImageIO.read(ByteArrayInputStream(bytes))
        val width = map[PreviewString.WIDTH]!!.toInt()
        val height = map[PreviewString.HEIGHT]!!.toInt()
        val widthNetSize = width / 8
        val heightNetSize = height / 8
        val boardSize = 8

        val boardImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d: Graphics2D = boardImage.createGraphics()

        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val x = col * widthNetSize
                val y = row * heightNetSize

                if ((row + col) % 2 == 0) {
                    g2d.drawImage(originalImage, x, y, widthNetSize, heightNetSize, null)
                } else {
                    g2d.color = Color.WHITE
                    g2d.fillRect(x, y, widthNetSize, heightNetSize)
                }
            }
        }
        g2d.dispose()
        val baos = ByteArrayOutputStream()
        ImageIO.write(boardImage, "png", baos)
        return baos.toByteArray()
    }


    override fun pngJpegGeneratePreview(width: Int, height: Int, bytes: ByteArray): ByteArray {
        val originalImage = ImageIO.read(ByteArrayInputStream(bytes))
        val widthNetSize = width / 8
        val heightNetSize = height / 8
        val boardSize = 8

        val boardImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d: Graphics2D = boardImage.createGraphics()

        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val x = col * widthNetSize
                val y = row * heightNetSize

                if ((row + col) % 2 == 0) {
                    val subImage = originalImage.getSubimage(x, y, widthNetSize, heightNetSize)
                    g2d.drawImage(subImage, x, y, widthNetSize, heightNetSize, null)
                } else {
                    g2d.color = Color.WHITE
                    g2d.fillRect(x, y, widthNetSize, heightNetSize)
                }
            }
        }
        g2d.dispose()
        val baos = ByteArrayOutputStream()
        ImageIO.write(boardImage, "png", baos)
        return baos.toByteArray()
    }

    override fun getPreview(type: String, width: Int, height: Int, bytes: ByteArray): ByteArray {
        var res: ByteArray
        when (type) {
            "image/jpeg", "image/png", "image/jpg" -> res = pngJpegGeneratePreview(width, height, bytes)
            else -> {
                res = ByteArray(0)
            }
        }
        return res
    }

}
