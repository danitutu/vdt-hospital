package vdt.hospital.scheduling

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

@Document
data class Appointment(
    @Id val id: UUID,
    val medic: Medic,
    val patient: Patient,
    val activity: Activity,
    val start: Instant,
    val end: Instant,
)