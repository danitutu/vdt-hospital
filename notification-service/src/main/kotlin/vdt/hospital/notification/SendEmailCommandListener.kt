package vdt.hospital.notification

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SendEmailCommandListener(
    @Value("\${mail.config.default.from}") val defaultFrom: String
) {

    @KafkaListener(topics = ["\${kafka.topic.command.send-email.name}"])
    fun listen(data: String) {
        val email = jacksonObjectMapper().readValue(data, Email::class.java)

        with(email) {
            val from = from ?: defaultFrom

            logger.info(
                """Simulating email sending
              From: $from
              To: $to
              Content Type: $contentType
              Body:
            $body
        """.trimIndent()
            )
        }
    }

    data class Email(
        val from: String?,
        val to: List<String>,
        val contentType: ContentType,
        val body: String,
    ) {
        enum class ContentType {
            TEXT
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SendEmailCommandListener::class.java)
    }
}
