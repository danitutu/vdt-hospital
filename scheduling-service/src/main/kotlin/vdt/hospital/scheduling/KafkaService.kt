package vdt.hospital.scheduling

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaService(
    private val template: KafkaTemplate<String, String>,
    @Value("\${kafka.topic.command.notification.send-email.name}") private val notificationSendEmailCommandTopic: String,
) {
    fun sendEmailMessage(data: Email) {
        template.send(notificationSendEmailCommandTopic, jacksonObjectMapper().writeValueAsString(data))
    }

}