package vdt.hospital.medic

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*


@Repository
interface MedicRepository : ReactiveCrudRepository<Medic, UUID> {

    suspend fun findByPin(pin: String): Medic?

}

