package com.ms.kafka.demo.consumer

import com.ms.kafka.demo.TriggerEvent
import com.ms.kafka.demo.producer.KafkaProducer
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author Ivelin Dimitrov
 */
@Component
class KafkaConsumer(
    private val kafkaProducer: KafkaProducer,
    private val restTemplate: RestTemplate
) {
    private val log = LoggerFactory.getLogger(KafkaConsumer::class.java)

    @KafkaListener(topics = ["trigger"])
    fun processMessage(event: TriggerEvent) {
        log.info("Received: ${event.id}")
        kafkaProducer.send("jobOne", event)
    }

    @KafkaListener(topics = ["jobOne"])
    fun processJobOne(event: TriggerEvent) {
        val body = restTemplate.exchange(
            "https://web.test-shop-volkswagen-we.com/countries/mapping/list",
            HttpMethod.GET,
            HttpEntity<String>(HttpHeaders()),
            String::class.java
        ).body ?: ""
        log.info("Successfuly executed job one + key ${event.id}, data: $body")
        kafkaProducer.send("jobTwo", event)
    }

    @KafkaListener(topics = ["jobTwo"])
    fun processJobTwo(event: TriggerEvent) {
        val body = restTemplate.exchange(
            "https://web.test-shop-volkswagen-we.com/countries/mapping/list",
            HttpMethod.GET,
            HttpEntity<String>(HttpHeaders()),
            String::class.java
        ).body ?: ""
        log.info("Successfuly executed job Two + key ${event.id}, data: $body")
    }
}