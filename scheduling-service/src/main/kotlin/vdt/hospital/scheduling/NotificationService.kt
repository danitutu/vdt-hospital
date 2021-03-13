package vdt.hospital.scheduling

import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val kafkaService: KafkaService,
) {

    fun sendAppointmentCreatedNotification(appointment: Appointment) {
        val email = Email(
            to = listOf(appointment.patient.email),
            contentType = Email.ContentType.TEXT,
            body = with(appointment) {
                """
                Hello ${patient.fullName},
                
                You have a new scheduled appointment:
                 
                Start date: $start 
                Activity: ${activity.name}
                Specialty: ${activity.specialty}
                Medic: ${medic.fullName}
                Estimated duration: ${activity.duration}
                
                Best regards,
                Your hospital
            """.trimIndent()
            }
        )
        kafkaService.sendEmailMessage(email)
    }

}