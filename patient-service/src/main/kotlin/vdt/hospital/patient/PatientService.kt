package vdt.hospital.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PatientService(
    private val patientRepository: PatientRepository,
) {

    @Transactional
    suspend fun createNewUser(input: Patient): Patient {
        validationWrapper {
            check(patientRepository.findByPin(input.pin) == null) { "A patient with the same PIN '${input.pin}' already exists." }
        }

        return patientRepository.save(input).awaitSingle()
    }

    @Transactional
    suspend fun updatePatient(patientId: UUID, input: Patient): Patient {
        val dbPatient = patientRepository.findById(patientId).awaitSingle()

        val updatedPatient = with(input) {
            dbPatient.copy(
                firstName = firstName,
                lastName = lastName,
                pin = pin,
                address = address,
                birthDate = birthDate,
            )
        }

        return patientRepository.save(updatedPatient).awaitSingle()
    }

    suspend fun searchPatients(): Flow<Patient> {
        return patientRepository.findAll().asFlow()
    }

    suspend fun findPatientById(patientId: UUID): Patient? {
        return patientRepository.findById(patientId).awaitSingleOrNull()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

}
