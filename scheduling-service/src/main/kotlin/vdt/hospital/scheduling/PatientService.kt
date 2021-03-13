package vdt.hospital.scheduling

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange
import java.util.*

@Service
class PatientService(
    @Value("\${SERVICE_PATIENT_API_PATH}") patientApiPath: String,
    private val webClient: WebClient = WebClient.create(patientApiPath)
) {
    private val logger = LoggerFactory.getLogger(PatientService::class.java)

    suspend fun findById(patientId: UUID): Patient? {
        val entity = webClient.get()
            .uri("/$patientId")
            .awaitExchange { clientResponse ->
                clientResponse.awaitEntity<Patient>()
            }
        if (entity.statusCode != HttpStatus.OK) {
            logger.error("Failed to retrieve the patient by ID '$patientId'. Response status: ${entity.statusCodeValue}")
            return null
        }
        return entity.body
    }
}
