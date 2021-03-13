package vdt.hospital.patient

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class PatientDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val pin: String,
    val address: String?,
    val birthDate: LocalDate,
    val medicalRecords: List<MedicalRecordDto>? = null
)

data class CreatePatientDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val pin: String,
    val address: String?,
    val birthDate: LocalDate,
) {
    fun toPatient() =
        Patient(
            id = UUID.randomUUID(),
            firstName = firstName,
            lastName = lastName,
            email = email,
            pin = pin,
            address = address,
            birthDate = birthDate,
        )
}

data class UpdatePatientDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val pin: String,
    val address: String?,
    val birthDate: LocalDate,
) {
    fun toPatient() =
        Patient(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            pin = pin,
            address = address,
            birthDate = birthDate,
        )
}

data class MedicalRecordDto(
    val id: UUID,
    val details: String,
    val medic: MedicDto,
    val createdOn: LocalDateTime
)

data class MedicDto(
    val id: UUID,
    val name: String,
)