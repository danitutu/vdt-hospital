package vdt.hospital.medic

import java.time.LocalDate
import java.util.*

data class MedicDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val pin: String,
    val address: String?,
    val birthDate: LocalDate,
    val specialty: Specialty,
) {
    companion object {
        fun fromMedic(medic: Medic) = with(medic) {
            MedicDto(
                id = id,
                firstName = firstName,
                lastName = lastName,
                pin = pin,
                address = address,
                birthDate = birthDate,
                specialty = specialty
            )
        }
    }
}

interface ChangeMedicDto {
    val firstName: String
    val lastName: String
    val address: String?
    val birthDate: LocalDate
    val specialty: Specialty
}

data class CreateMedicDto(
    override val firstName: String,
    override val lastName: String,
    val pin: String,
    override val address: String?,
    override val birthDate: LocalDate,
    override val specialty: Specialty,
): ChangeMedicDto {
    fun toMedic() =
        Medic(
            id = UUID.randomUUID(),
            firstName = firstName,
            lastName = lastName,
            pin = pin,
            address = address,
            birthDate = birthDate,
            specialty = specialty,
        )
}

data class UpdateMedicDto(
    override val firstName: String,
    override val lastName: String,
    override val address: String?,
    override val birthDate: LocalDate,
    override val specialty: Specialty,
) : ChangeMedicDto

data class ValidationErrorDto(
    val errorCode: Code,
    val errorMessage: String,
) {
    enum class Code {
        MEDIC_ALREADY_EXISTS,
        INVALID_ADDRESS_LENGTH,
        INVALID_BIRTH_DATE,
        MISSING_FIRST_NAME,
        MISSING_LAST_NAME,
        INVALID_FIRST_NAME_LENGTH,
        INVALID_LAST_NAME_LENGTH,
        INVALID_ADDRESS,
        MEDIC_NOT_FOUND,
    }

    companion object {
        fun fromValidationError(e: CreateNewMedicError): ValidationErrorDto {
            return when (e) {
                is CreateNewMedicError.MedicValidationFailedError -> handleMedicValidationResult(e.medicValidationResult)
                is CreateNewMedicError.MedicAlreadyExists -> ValidationErrorDto(
                    Code.MEDIC_ALREADY_EXISTS,
                    "Medic already exists"
                )
            }
        }
        fun fromValidationError(e: UpdateMedicError): ValidationErrorDto {
            return when (e) {
                is UpdateMedicError.MedicValidationFailedError -> handleMedicValidationResult(e.medicValidationResult)
                is UpdateMedicError.MedicNotFound -> ValidationErrorDto(
                    Code.MEDIC_NOT_FOUND,
                    "Medic not found"
                )
            }
        }

        private fun handleMedicValidationResult(medicValidationResult: MedicValidationResult) =
            when (medicValidationResult) {
                MedicValidationResult.InvalidAddressLength -> ValidationErrorDto(
                    Code.INVALID_ADDRESS_LENGTH,
                    "Invalid 'address' length. Length cannot be greater than 500 chars."
                )
                MedicValidationResult.InvalidBirthDate -> ValidationErrorDto(
                    Code.INVALID_BIRTH_DATE,
                    "Invalid 'birthDate'"
                )
                MedicValidationResult.MissingFirstName -> ValidationErrorDto(
                    Code.MISSING_FIRST_NAME,
                    "First name is missing"
                )
                MedicValidationResult.MissingLastName -> ValidationErrorDto(
                    Code.MISSING_LAST_NAME,
                    "Last name is missing"
                )
                MedicValidationResult.InvalidFirstNameLength -> ValidationErrorDto(
                    Code.INVALID_FIRST_NAME_LENGTH,
                    "Invalid 'firstName' length. Length must be between 1 and 50 chars."
                )
                MedicValidationResult.InvalidLastNameLength -> ValidationErrorDto(
                    Code.INVALID_LAST_NAME_LENGTH,
                    "Invalid 'lastName' length. Length must be between 1 and 50 chars."
                )
                MedicValidationResult.InvalidAddress -> ValidationErrorDto(
                    Code.INVALID_ADDRESS,
                    "Invalid address"
                )
            }
    }
}