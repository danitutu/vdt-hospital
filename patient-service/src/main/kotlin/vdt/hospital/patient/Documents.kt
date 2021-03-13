package vdt.hospital.patient

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

private val inferiorBirthDateLimit = LocalDate.of(1900, 1, 1)

@Document
data class Patient(
    @Id val id: UUID,
    val firstName: String,
    val lastName: String,
    val pin: String,
    val email: String,
    val address: String?,
    val birthDate: LocalDate,
    val medicalRecords: List<MedicalRecord>? = null
) {
    init {
        validationWrapper {
            require(firstName.length in 1..50) { "Invalid 'firstName' length. Length must be between 1 and 50 chars." }
            require(lastName.length in 1..50) { "Invalid 'lastName' length. Length must be between 1 and 50 chars." }
            address?.let {
                require(it.length <= 500) { "Invalid 'address' length. Length cannot be greater than 500 chars." }
            }
            require(birthDate >= inferiorBirthDateLimit && birthDate <= LocalDate.now()) { "Invalid 'birthDate'" }
        }
    }
}

@Document
data class MedicalRecord(
    @Id val id: UUID,
    val details: String,
    val medic: Medic,
    val createdOn: LocalDateTime
)

@Document
data class Medic(
    @Id val id: UUID,
    val name: String,
)