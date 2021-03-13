package vdt.hospital.scheduling

data class ValidationErrorDto(
    val errorCode: Code,
    val errorMessage: String,
) {
    enum class Code {
        MEDIC_NOT_FOUND,
        PATIENT_NOT_FOUND,
        SLOT_UNAVAILABLE,
        FUTURE_START_DATE,
    }

    companion object {
        fun fromValidationError(e: ValidationError): ValidationErrorDto {
            return when (e) {
                ValidationError.MedicNotFound -> ValidationErrorDto(
                    Code.MEDIC_NOT_FOUND,
                    "Medic not found"
                )
                ValidationError.PatientNotFound -> ValidationErrorDto(
                    Code.PATIENT_NOT_FOUND,
                    "Patient not found"
                )
                ValidationError.SlotUnavailable -> ValidationErrorDto(
                    Code.SLOT_UNAVAILABLE,
                    "Chosen slot is unavailable"
                )
                ValidationError.StartDateInThePast -> ValidationErrorDto(
                    Code.FUTURE_START_DATE,
                    "Start date is in the past"
                )
            }
        }
    }
}