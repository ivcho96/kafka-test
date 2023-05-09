package com.ms.kafka.demo.consumer

import com.ms.kafka.demo.TriggerEvent
import com.ms.kafka.demo.producer.KafkaProducer
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.retry.annotation.Backoff
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

    @RetryableTopic(
        attempts = "25",
        timeout = "25000",
        backoff = Backoff(
            value = 1000L,
            delay = 1000L,
            maxDelay = 5000L
        )
    )
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

    @RetryableTopic(
        attempts = "6",
        backoff = Backoff(
            delay = 10000L,
            multiplier = 3.0
//            ,maxDelay = 30000L
        )
    )
    @KafkaListener(topics = ["jobTwo"])
    fun processJobTwo(event: TriggerEvent) {
        log.info("Received jobTwo ${event.id} + time: ${System.currentTimeMillis()}")
        try {
            val body = restTemplate.exchange(
                "https://web.test-shop-volkswagen-we.com/countries/mapping/list",
                HttpMethod.GET,
                HttpEntity<String>(HttpHeaders()),
                String::class.java
            ).body ?: ""
            log.info("Successfuly executed job Two + key ${event.id}, data: $body")
        } catch (
            e: Exception
        ) {
            log.info("Failed jobTwo ${event.id} + time: ${System.currentTimeMillis()}")
            throw e
        }
        kafkaProducer.send("jobThree", event)
    }

    @RetryableTopic(
        attempts = "25",
        timeout = "25000",
        backoff = Backoff(
            value = 2000L,
            delay = 5000L,
            maxDelay = 20000L
        )
    )@KafkaListener(topics = ["jobThree"])
    fun processJobThree(event: TriggerEvent) {
        val body = restTemplate.exchange(
            "https://web.test-shop-volkswagen-we.com/countries/mapping/list",
            HttpMethod.GET,
            HttpEntity<String>(HttpHeaders()),
            String::class.java
        ).body ?: ""
        log.info("Successfuly executed job Three + key ${event.id}, data: $body")
    }

    @DltHandler
    fun dlt(event: TriggerEvent, @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String) {
        log.info("$topic with key ${event.id} is DEAD")
    }
}