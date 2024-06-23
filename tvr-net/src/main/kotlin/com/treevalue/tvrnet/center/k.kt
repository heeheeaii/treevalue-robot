package com.treevalue.tvrnet.center

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.*
import java.util.concurrent.ConcurrentHashMap


@SpringBootApplication
@RestController
@RequestMapping("/central")
class CentralServerApplication {
    private val serverRegistry: MutableMap<String, String> =ConcurrentHashMap()
    @PostMapping("/register")
    fun registerServer(@RequestParam address: String): String {
        serverRegistry[address] = address
        return "Registered: $address"
    }

    @get:GetMapping("/getServer")
    val server: String
        get() = java.lang.String.join(",", serverRegistry.values)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(CentralServerApplication::class.java, *args)
        }
    }
}

