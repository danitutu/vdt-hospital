package vdt.hospital.medic

import arrow.core.Either
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.util.*

internal class MedicServiceUpdateTest {
    private val medicRepository = mockk<MedicRepository>(relaxed = true)
    private val medicService = MedicService(medicRepository)

    private val id = UUID.randomUUID()

    @Test
    fun success() {
        val birthDate = LocalDate.now()

        val input = createInput(birthDate)

        val medic = createSavedEntity()

        mockFindById()
        mockSave(medic)

        val result = callService(id, input)

        assertEquals(Either.Right(medic), result)

        verifyRepositorySave(birthDate)
    }

    @Test
    fun `WHEN medic validation fails THEN left error`() {
        val birthDate = LocalDate.now()

        val input = createInput(birthDate).copy(firstName = "")

        val result = callService(id, input)

        assertEquals(
            Either.Left(UpdateMedicError.MedicValidationFailedError(MedicValidationResult.MissingFirstName)),
            result
        )
    }

    @Test
    fun `WHEN patient cannot be found THEN left error`() {
        val birthDate = LocalDate.now()

        val input = createInput(birthDate)

        every { medicRepository.findById(id) } returns Mono.empty()

        val result = callService(id, input)

        assertEquals(Either.Left(UpdateMedicError.MedicNotFound), result)
    }

    private fun mockFindById() {
        every { medicRepository.findById(id) } returns Mono.just(createSavedEntity())
    }

    private fun createInput(birthDate: LocalDate): UpdateMedicDto {
        return UpdateMedicDto(
            firstName = "dani",
            lastName = "tutu",
            address = "a street beyond a street",
            birthDate = birthDate,
            specialty = Specialty.ANESTHESIOLOGY
        )
    }

    private fun createSavedEntity(): Medic {
        return Medic( // different than input on purpose
            id = id,
            firstName = "d",
            lastName = "t",
            pin = "1234567890123",
            address = "a",
            birthDate = LocalDate.now(),
            specialty = Specialty.EMERGENCY_MEDICINE
        )
    }

    private fun mockSave(medic: Medic) {
        every { medicRepository.save(any()) } returns Mono.just(medic)
    }

    private fun callService(id: UUID, input: UpdateMedicDto) = runBlocking {
        medicService.update(id, input)
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