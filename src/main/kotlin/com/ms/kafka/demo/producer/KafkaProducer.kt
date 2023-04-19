package com.ms.kafka.demo.producer

import com.ms.kafka.demo.TriggerEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

/**
 * @author Ivelin Dimitrov
 */
@Component
class KafkaProducer(private val kafkaTemplate: KafkaTemplate<String, TriggerEvent>) {
    private val log = LoggerFactory.getLogger(KafkaProducer::class.java)

    fun send(topic: String = "trigger",event: TriggerEvent) {
        kafkaTemplate.send(topic, event)
    }

}