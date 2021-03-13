package vdt.hospital.scheduling

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*


@Repository
interface AppointmentRepository : ReactiveCrudRepository<Appointment, UUID> {

    suspend fun existsByStartBeforeOrEndAfter(start: Instant, end: Instant): Boolean

}

