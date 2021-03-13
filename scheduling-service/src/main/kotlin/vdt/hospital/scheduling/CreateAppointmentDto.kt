package vdt.hospital.scheduling

import java.time.ZonedDateTime
import java.util.*

data class CreateAppointmentDto(
    val medicId: UUID,
    val patientId: UUID,
    val activity: Activity,
    val start: ZonedDateTime
)
