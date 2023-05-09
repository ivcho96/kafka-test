package com.ms.kafka.demo.producer

import com.ms.kafka.demo.TriggerEvent
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

/**
 * @author Ivelin Dimitrov
 */

@RestController
class KafkaController(
    val kafkaProducer: KafkaProducer
) {
    private val log = LoggerFactory.getLogger(KafkaController::class.java)

    @GetMapping("/trigger/{id}")
    fun triggerEvents(
        @PathVariable id: Int
    ) {
        log.info("Received: $id")
        kafkaProducer.send(
            event = TriggerEvent(id = id)
        )
    }
}