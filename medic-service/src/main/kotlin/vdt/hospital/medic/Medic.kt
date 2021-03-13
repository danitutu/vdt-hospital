package vdt.hospital.medic

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*

@Document
data class Medic(
    @Id val id: UUID,
    val firstName: String,
    val lastName: String,
    val pin: String,
    val address: String?,
    val birthDate: LocalDate,
    val specialty: Specialty,
)