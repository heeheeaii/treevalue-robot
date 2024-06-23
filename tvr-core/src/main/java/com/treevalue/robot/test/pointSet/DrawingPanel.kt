package com.treevalue.robot.test.pointSet

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel

class DrawingPanel : JPanel() {
    private val points: MutableList<Point> = mutableListOf()

    init {
        background = Color.WHITE
        val mouseAdapter = object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                points.add(Point(e.x, e.y))
                repaint()
            }

            override fun mouseDragged(e: MouseEvent) {
                points.add(Point(e.x, e.y))
                repaint()
            }
        }
        addMouseListener(mouseAdapter)
        addMouseMotionListener(mouseAdapter)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.color = Color.BLACK
        for (i in 1 until points.size) {
            val p1 = points[i - 1]
            val p2 = points[i]
            g.drawLine(p1.x, p1.y, p2.x, p2.y)
        }
    }

    fun savePointsToFile(filename: String) {
        File(filename).bufferedWriter().use { out ->
            points.forEach { point ->
                out.write("${point.x}, ${point.y}\n")
            }
        }
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val frame = JFrame("Draw and Save Points")
            val drawingPanel = DrawingPanel()

            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.contentPane.add(drawingPanel)
            frame.setSize(400, 400)
            frame.isVisible = true

            val saveButton = JButton("Save Points")
            saveButton.addActionListener {
                drawingPanel.savePointsToFile("D:\\treevalue\\treevalue-robot\\tvr-core\\src\\main\\java\\com\\treevalue\\robot\\test\\points.txt")
                JOptionPane.showMessageDialog(frame, "Points saved to points.txt")
            }

            frame.contentPane.add(saveButton, BorderLayout.SOUTH)
        }
    }
}
