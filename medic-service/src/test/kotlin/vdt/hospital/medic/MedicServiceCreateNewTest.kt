package vdt.hospital.medic

import arrow.core.Either
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.util.*

internal class MedicServiceCreateNewTest {
    private val medicRepository = mockk<MedicRepository>(relaxed = true)
    private val medicService = MedicService(medicRepository)

    @Test
    fun success() {
        val birthDate = LocalDate.now()

        val input = createInput(birthDate)

        val medic = createSavedEntity()

        mockFindByPin(input)
        mockSave(medic)

        val result = callService(input)

        assertEquals(Either.Right(medic), result)

        verifyRepositorySave(birthDate)
    }

    @Test
    fun `WHEN medic validation fails THEN left error`() {
        val birthDate = LocalDate.now()

        val input = createInput(birthDate).copy(firstName = "")

        val result = callService(input)

        assertEquals(
            Either.Left(CreateNewMedicError.MedicValidationFailedError(MedicValidationResult.MissingFirstName)),
            result
        )
    }

    @Test
    fun `WHEN pin already in use THEN left error`() {
        val birthDate = LocalDate.now()

        val input = createInput(birthDate)

        coEvery { medicRepository.findByPin(input.pin) } returns createSavedEntity()

        val result = callService(input)

        assertEquals(Either.Left(CreateNewMedicError.MedicAlreadyExists), result)
    }

    private fun createInput(birthDate: LocalDate): CreateMedicDto {
        return CreateMedicDto(
            firstName = "dani",
            lastName = "tutu",
            pin = "1234567890123",
            address = "a street beyond a street",
            birthDate = birthDate,
            specialty = Specialty.ANESTHESIOLOGY
        )
    }

    private fun createSavedEntity(): Medic {
        return Medic( // different than input on purpose
            id = UUID.randomUUID(),
            firstName = "",
            lastName = "",
            pin = "",
            address = "",
            birthDate = LocalDate.now(),
            specialty = Specialty.ANESTHESIOLOGY
        )
    }

    private fun mockSave(medic: Medic) {
        every { medicRepository.save(any()) } returns Mono.just(medic)
    }

    private fun mockFindByPin(input: CreateMedicDto) {
        coEvery { medicRepository.findByPin(input.pin) } returns null
    }

    private fun callService(input: CreateMedicDto) = runBlocking {
        medicService.createNew(input)
    }

    private fun verifyRepositorySave(birthDate: LocalDate) {
        verify {
            medicRepository.save(match {
                it == Medic(
                    id = it.id,
                    firstName = "dani",
                    lastName = "tutu",
                    pin = "1234567890123",
                    address = "a street beyond a street",
                    birthDate = birthDate,
                    specialty = Specialty.ANESTHESIOLOGY
                )
            })
        }
    }
}