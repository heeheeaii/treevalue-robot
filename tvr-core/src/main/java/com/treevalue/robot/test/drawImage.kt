package com.treevalue.robot.test
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel

class ImagePanel(private var image: BufferedImage) : JPanel() {
    private val points = mutableListOf<Pair<Int, Int>>()

    init {
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                points.add(Pair(e.x, e.y))
                repaint()
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                points.add(Pair(e.x, e.y))
                repaint()
            }
        })
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.drawImage(image, 0, 0, this)
        g2d.color = Color.RED
        g2d.stroke = BasicStroke(3f)
        for (i in 0 until points.size - 1) {
            g2d.drawLine(points[i].first, points[i].second, points[i + 1].first, points[i + 1].second)
        }
    }

    fun savePoints(filePath: String) {
        val lines = points.map { "${it.first},${it.second}" }
        Files.write(File(filePath).toPath(), lines)
    }
}

fun main() {
    // Load image
    val imagePath = "D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\screen.png"
    val image = ImageIO.read(File(imagePath))

    // Create and set up JFrame
    val frame = JFrame("Image Drawer")
    val imagePanel = ImagePanel(image)
    frame.contentPane.add(imagePanel)
    frame.setSize(image.width, image.height)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true

    Runtime.getRuntime().addShutdownHook(Thread {
        val savePath = "D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\paint3.txt"
        imagePanel.savePoints(savePath)
        println("Points saved to $savePath")
    })
}
