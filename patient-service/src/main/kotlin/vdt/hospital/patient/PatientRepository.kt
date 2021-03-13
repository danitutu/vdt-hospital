package vdt.hospital.patient

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*


@Repository
interface PatientRepository : ReactiveCrudRepository<Patient, UUID> {

    suspend fun findByPin(pin: String): Patient?

}

