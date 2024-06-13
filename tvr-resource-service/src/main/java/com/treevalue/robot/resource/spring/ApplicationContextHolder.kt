package com.treevalue.robot.resource.spring
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component("contextHolder")
class ApplicationContextHolder : ApplicationContextAware {
    companion object {
        private var context: ApplicationContext? = null

        fun getBean(beanName: String): Any {
            return context!!.getBean(beanName)
        }

        fun <T> getBean(beanName: String, requiredType: Class<T>): T {
            return context!!.getBean(beanName, requiredType)
        }

        fun <T> getBean(type:Class<T>):T{
            return context!!.getBean(type)
        }
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }
}
