package vdt.hospital.medic

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class MedicValidatorValidateMedicTest {

    private val input = UpdateMedicDto("fn", "ln", "addr", LocalDate.now(), Specialty.ANESTHESIOLOGY)

    @Test
    fun `WHEN valid input THEN result should be null`() {
        val result = validateMedic(input)
        assertNull(result)
    }

    @Test
    fun `WHEN first name is empty THEN error`() {
        val result = validateMedic(input.copy(firstName = ""))
        assertSame(MedicValidationResult.MissingFirstName, result)
    }

    @Test
    fun `WHEN first name is blank THEN error`() {
        val result = validateMedic(input.copy(firstName = "   "))
        assertSame(MedicValidationResult.MissingFirstName, result)
    }

    @Test
    fun `WHEN first name greater than max limit THEN error`() {
        val result = validateMedic(input.copy(firstName = "abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdea"))
        assertSame(MedicValidationResult.InvalidFirstNameLength, result)
    }

    @Test
    fun `WHEN last name is empty THEN error`() {
        val result = validateMedic(input.copy(lastName = ""))
        assertSame(MedicValidationResult.MissingLastName, result)
    }

    @Test
    fun `WHEN last name is blank THEN error`() {
        val result = validateMedic(input.copy(lastName = "   "))
        assertSame(MedicValidationResult.MissingLastName, result)
    }

    @Test
    fun `WHEN last name greater than max limit THEN error`() {
        val result = validateMedic(input.copy(lastName = "abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdea"))
        assertSame(MedicValidationResult.InvalidLastNameLength, result)
    }

    @Test
    fun `WHEN address is blank THEN error`() {
        val result = validateMedic(input.copy(address = "   "))
        assertSame(MedicValidationResult.InvalidAddress, result)
    }

    @Test
    fun `WHEN address greater than max limit THEN error`() {
        val result = validateMedic(input.copy(address = "abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdea"))
        assertSame(MedicValidationResult.InvalidAddressLength, result)
    }

    @Test
    fun `WHEN address is null THEN ok`() {
        val result = validateMedic(input.copy(address = null))
        assertNull(result)
    }

    @Test
    fun `WHEN birthday is before smallest limit THEN error`() {
        val result = validateMedic(input.copy(birthDate = LocalDate.of(1890, 1, 1)))
        assertSame(MedicValidationResult.InvalidBirthDate, result)
    }

    @Test
    fun `WHEN birthday is after current date THEN error`() {
        val result = validateMedic(input.copy(birthDate = LocalDate.now().plusDays(1)))
        assertSame(MedicValidationResult.InvalidBirthDate, result)
    }
}