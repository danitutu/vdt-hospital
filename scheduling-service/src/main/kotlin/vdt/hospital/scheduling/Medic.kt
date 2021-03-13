package vdt.hospital.scheduling

import java.util.*

data class Medic(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val specialty: Specialty,
) {
    val fullName: String = "$lastName $firstName"
}