package com.ms.kafka.demo.config

import lombok.extern.slf4j.Slf4j
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.stereotype.Component


/**
 * @author Ivelin Dimitrov
 */
@Component
@Slf4j
class KafkaProps {
//    private val TRUSTSTORE_JKS = "/var/private/ssl/kafka.client.truststore.jks"
    private val SASL_PROTOCOL = "SASL_SSL"
    private val SCRAM_SHA_256 = "SCRAM-SHA-256"
    private val jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";"
    private val consJaasCfg = String.format(jaasTemplate, "alice", "alice-secret")


    @Value(value = "\${spring.kafka.bootstrap-servers:}")
    private val bootstrapAddress: String? = null

    @Value(value = "\${spring.kafka.consumer.key-deserializer:}")
    private val consumerKeyDeserializer: String? = null

    @Value(value = "\${spring.kafka.consumer.value-deserializer:}")
    private val consumerValueDeserializer: String? = null

    @Value(value = "\${spring.kafka.consumer.group-id:}")
    private val consumerGroupId: String? = null

    @Value(value = "\${spring.kafka.producer.key-serializer:}")
    private val producerKeySerializer: String? = null

    @Value(value = "\${spring.kafka.producer.value-serializer:}")
    private val producerValueSerializer: String? = null

    @Value(value = "\${spring.kafka.consumer.properties.spring.json.trusted.packages:}")
    private val trustedPackages: String? = null


    fun consumerProps(): Map<String, String?> {
        val props: MutableMap<String, String?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = consumerGroupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = consumerKeyDeserializer
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = consumerValueDeserializer
        props[JsonDeserializer.TRUSTED_PACKAGES] = trustedPackages
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        props["sasl.mechanism"] = SCRAM_SHA_256
        props["sasl.jaas.config"] = consJaasCfg
        props["security.protocol"] = SASL_PROTOCOL
//        props["ssl.truststore.location"] = TRUSTSTORE_JKS
//        props["ssl.truststore.password"] = "password"
//        props["ssl.endpoint.identification.algorithm"] = ""
        return props
    }
}