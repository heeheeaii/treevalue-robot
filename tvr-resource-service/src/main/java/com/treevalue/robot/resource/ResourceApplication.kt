package com.treevalue.robot.resource

import com.treevalue.robot.resource.spring.ApplicationContextHolder
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.scheduling.annotation.EnableAsync


@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableCaching
@EnableFeignClients
//note: put on main class
@MapperScan("com.treevalue.robot.resource.pgsql.mapper")
open class ResourceApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val context: ConfigurableApplicationContext =
                SpringApplicationBuilder(ResourceApplication::class.java).run(*args)
            val holder = context.getBean(ApplicationContextHolder::class.java)
            holder.setApplicationContext(context)
        }
    }
}

