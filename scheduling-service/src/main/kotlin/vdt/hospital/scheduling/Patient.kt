package vdt.hospital.scheduling

import java.util.*

data class Patient(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
) {
    val fullName: String = "$lastName $firstName"
}