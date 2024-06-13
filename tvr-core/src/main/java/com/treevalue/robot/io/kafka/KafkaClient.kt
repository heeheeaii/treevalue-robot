package com.treevalue.robot.io.kafka

import com.treevalue.robot.io.FileIOUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.io.Closeable
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.util.*

class KafkaClient<T, V> : Closeable {
    private var producer: KafkaProducer<T, V>? = null
    private var fileConfig: Properties? = null
    val TOPIC = "topic"
    private val DURATION = 100L
    private val POOLSIZE = 10
    private val DISPATCHER = newFixedThreadPoolContext(POOLSIZE, "CoroutinePool")
    private val SCOPE = CoroutineScope(DISPATCHER)

    constructor(configPath: String? = null, config: Properties? = null) {
        if (config == null && configPath == null) throw Exception("don't have any config")
        var config1 = Properties()
        if (configPath != null) {
            fileConfig = readConfig(configPath)
            fileConfig?.let { config1.putAll(it) }
        }
        if (config != null) {
            config1.putAll(config)
        }
        producer = KafkaProducer(config1)
    }

    fun produce(key: T, value: V, topic: String? = null) {
        producer?.send(ProducerRecord(topic ?: (fileConfig?.getProperty(TOPIC) as String), key, value))
    }

    fun consume(func: suspend (ConsumerRecords<T, V>) -> Unit) {
        val consumer: KafkaConsumer<T, V> = KafkaConsumer(fileConfig)
        consumer.subscribe(Arrays.asList(fileConfig?.getProperty(TOPIC)))
        SCOPE.launch {
            while (true) {
                val records: ConsumerRecords<T, V> = consumer.poll(Duration.ofMillis(DURATION))
                func(records)
                delay(DURATION)
            }
        }
    }


    @Throws(IOException::class)
    fun readConfig(configFile: String): Properties {
        if (!Files.exists(Paths.get(configFile))) {
            throw IOException("$configFile not found.")
        }
        val config = Properties()
        FileInputStream(configFile).use { inputStream -> config.load(inputStream) }
        return config
    }

    override fun close() {
        producer?.close()
    }
}

fun main() {
    var path: String = FileIOUtil.getSourceAbsolutePath("cloud/kafka-client.properties") ?: return
    val cli =
        KafkaClient<String, String>(path)
    cli.consume {
        for (record in it) {
            println(
                String.format(
                    "Consumed message from topic %s: key = %s value = %s", cli.TOPIC, record.key(), record.value()
                )
            )
        }
    }

    // Give some time for the consumer to start
    Thread.sleep(2000)

    cli.produce("key1", "test")

    // Give some time for the consumer to consume the message
    Thread.sleep(5000)
}
