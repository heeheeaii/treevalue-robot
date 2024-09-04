package com.treevalue

import com.treevalue.robot.v3.transform.Input
import com.treevalue.robot.v3.transform.impl.InputImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InputTest {

    val input: Input = InputImpl()

    @Test
    fun tensorTest() {
        val tensor = input.getSensors()[0]
        Thread.sleep((1/30.0*1000000/2).toLong())
        val tensor2 = input.getSensors()[0]
        Thread.sleep((1.2/30.0*1000000/2).toLong())
        val tensor3 = input.getSensors()[0]
        Assertions.assertTrue(tensor == tensor2)
        Assertions.assertFalse(tensor2 == tensor3)
    }
}
