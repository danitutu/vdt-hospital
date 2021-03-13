package vdt.hospital.medic

import arrow.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MedicService(
    private val medicRepository: MedicRepository,
) {

    @Transactional
    suspend fun createNew(input: CreateMedicDto): Either<CreateNewMedicError, Medic> {
        validateMedic(input)?.let { return CreateNewMedicError.MedicValidationFailedError(it).left() }

        if (medicRepository.findByPin(input.pin) != null) return CreateNewMedicError.MedicAlreadyExists.left()

        return medicRepository.save(input.toMedic()).awaitSingle().right()
    }

    @Transactional
    suspend fun update(medicId: UUID, input: UpdateMedicDto): Either<UpdateMedicError, Medic> {
        validateMedic(input)?.let { return UpdateMedicError.MedicValidationFailedError(it).left() }

        val dbPatient =
            medicRepository.findById(medicId).awaitSingleOrNull() ?: return UpdateMedicError.MedicNotFound.left()

        val updatedPatient = with(input) {
            dbPatient.copy(
                firstName = firstName,
                lastName = lastName,
                address = address,
                birthDate = birthDate,
                specialty = specialty,
            )
        }

        return medicRepository.save(updatedPatient).awaitSingle().right()
    }

    suspend fun search(): Flow<Medic> {
        return medicRepository.findAll().asFlow()
    }

    suspend fun findById(id: UUID): Medic? {
        return medicRepository.findById(id).awaitSingleOrNull()
    }

}

sealed class CreateNewMedicError {
    object MedicAlreadyExists : CreateNewMedicError()
    data class MedicValidationFailedError(val medicValidationResult: MedicValidationResult) : CreateNewMedicError()
}

sealed class UpdateMedicError {
    object MedicNotFound : UpdateMedicError()
    data class MedicValidationFailedError(val medicValidationResult: MedicValidationResult) : UpdateMedicError()
}