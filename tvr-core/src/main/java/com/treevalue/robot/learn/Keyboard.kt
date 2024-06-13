package com.treevalue.robot.learn

import com.treevalue.robot.data.Singleton
import java.awt.Robot
import java.awt.event.KeyEvent

class Keyboard {
    private val robot = Singleton.getRobot()
    fun input(str: String) {
        str.toCharArray().forEach {
            typeCharacter(robot, it)
        }
    }

    private fun typeCharacter(robot: Robot, char: Char) {
        when (char) {
            in 'A'..'Z' -> {
                robot.keyPress(KeyEvent.VK_SHIFT)
                robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(char.code))
                robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(char.code))
                robot.keyRelease(KeyEvent.VK_SHIFT)
            }

            in 'a'..'z' -> {
                robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(char.code))
                robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(char.code))
            }

            in '0'..'9' -> {
                robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(char.code))
                robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(char.code))
            }

            else -> {
                when (char) {
                    ' ' -> typeSpecialKey(robot, KeyEvent.VK_SPACE)
                    '!' -> typeShiftedKey(robot, KeyEvent.VK_1)
                    '@' -> typeShiftedKey(robot, KeyEvent.VK_2)
                    '#' -> typeShiftedKey(robot, KeyEvent.VK_3)
                    '$' -> typeShiftedKey(robot, KeyEvent.VK_4)
                    '%' -> typeShiftedKey(robot, KeyEvent.VK_5)
                    '^' -> typeShiftedKey(robot, KeyEvent.VK_6)
                    '&' -> typeShiftedKey(robot, KeyEvent.VK_7)
                    '*' -> typeShiftedKey(robot, KeyEvent.VK_8)
                    '(' -> typeShiftedKey(robot, KeyEvent.VK_9)
                    ')' -> typeShiftedKey(robot, KeyEvent.VK_0)
                    '-' -> typeSpecialKey(robot, KeyEvent.VK_MINUS)
                    '_' -> typeShiftedKey(robot, KeyEvent.VK_MINUS)
                    '=' -> typeSpecialKey(robot, KeyEvent.VK_EQUALS)
                    '+' -> typeShiftedKey(robot, KeyEvent.VK_EQUALS)
                    '[' -> typeSpecialKey(robot, KeyEvent.VK_OPEN_BRACKET)
                    '{' -> typeShiftedKey(robot, KeyEvent.VK_OPEN_BRACKET)
                    ']' -> typeSpecialKey(robot, KeyEvent.VK_CLOSE_BRACKET)
                    '}' -> typeShiftedKey(robot, KeyEvent.VK_CLOSE_BRACKET)
                    '\\' -> typeSpecialKey(robot, KeyEvent.VK_BACK_SLASH)
                    '|' -> typeShiftedKey(robot, KeyEvent.VK_BACK_SLASH)
                    ';' -> typeSpecialKey(robot, KeyEvent.VK_SEMICOLON)
                    ':' -> typeShiftedKey(robot, KeyEvent.VK_SEMICOLON)
                    '\'' -> typeSpecialKey(robot, KeyEvent.VK_QUOTE)
                    '"' -> typeShiftedKey(robot, KeyEvent.VK_QUOTE)
                    ',' -> typeSpecialKey(robot, KeyEvent.VK_COMMA)
                    '<' -> typeShiftedKey(robot, KeyEvent.VK_COMMA)
                    '.' -> typeSpecialKey(robot, KeyEvent.VK_PERIOD)
                    '>' -> typeShiftedKey(robot, KeyEvent.VK_PERIOD)
                    '/' -> typeSpecialKey(robot, KeyEvent.VK_SLASH)
                    '?' -> typeShiftedKey(robot, KeyEvent.VK_SLASH)
                    else -> throw IllegalArgumentException("can't input char: $char")
                }
            }
        }
    }

    private fun typeSpecialKey(robot: Robot, keyCode: Int) {
        robot.keyPress(keyCode)
        robot.keyRelease(keyCode)
    }

    private fun typeShiftedKey(robot: Robot, keyCode: Int) {
        robot.keyPress(KeyEvent.VK_SHIFT)
        robot.keyPress(keyCode)
        robot.keyRelease(keyCode)
        robot.keyRelease(KeyEvent.VK_SHIFT)
    }
}
