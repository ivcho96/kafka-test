package com.ms.kafka.demo.config

import com.ms.kafka.demo.TriggerEvent
import com.ms.kafka.demo.consumer.KafkaConsumer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate


/**
 * @author Ivelin Dimitrov
 */
@Configuration
class KafkaConfig(
    val kafkaTemplate: KafkaTemplate<String, TriggerEvent>,
    val kafkaProps: KafkaProps
) {

//    @Bean("kafkaListenerContainerFactory")
//    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<Any, Any>? {
//        val concurrentKafkaListenerContainerFactory = ConcurrentKafkaListenerContainerFactory<Any, Any>()
//        concurrentKafkaListenerContainerFactory.consumerFactory = DefaultKafkaConsumerFactory(kafkaProps.consumerProps())
//        return concurrentKafkaListenerContainerFactory
//    }
}