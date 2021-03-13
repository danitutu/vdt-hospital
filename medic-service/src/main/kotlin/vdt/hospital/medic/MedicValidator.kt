package vdt.hospital.medic

import java.time.LocalDate

private val inferiorBirthDateLimit = LocalDate.of(1900, 1, 1)

fun validateMedic(
    input: ChangeMedicDto,
): MedicValidationResult? {
    with(input) {
        if (firstName.isBlank()) return MedicValidationResult.MissingFirstName
        if (lastName.isBlank()) return MedicValidationResult.MissingLastName
        if (checkLengthBetween1And50(firstName)) return MedicValidationResult.InvalidFirstNameLength
        if (checkLengthBetween1And50(lastName)) return MedicValidationResult.InvalidLastNameLength
        address?.let {
            if (it.isBlank()) return MedicValidationResult.InvalidAddress
            if (checkLengthGreaterThan500(it)) return MedicValidationResult.InvalidAddressLength
        }
        if (checkDateValidity(birthDate)) return MedicValidationResult.InvalidBirthDate
    }
    return null
}

private fun checkDateValidity(date: LocalDate) = date < inferiorBirthDateLimit || date > LocalDate.now()

private fun checkLengthBetween1And50(value: String) = value.length !in 1..50
private fun checkLengthGreaterThan500(value: String) = value.length > 500

sealed class MedicValidationResult {
    object MissingFirstName : MedicValidationResult()
    object MissingLastName : MedicValidationResult()
    object InvalidFirstNameLength : MedicValidationResult()
    object InvalidLastNameLength : MedicValidationResult()
    object InvalidAddressLength : MedicValidationResult()
    object InvalidAddress : MedicValidationResult()
    object InvalidBirthDate : MedicValidationResult()
}