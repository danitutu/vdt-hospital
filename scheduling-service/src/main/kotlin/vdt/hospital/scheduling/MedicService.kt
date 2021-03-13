package vdt.hospital.scheduling

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange
import java.util.*

@Service
class MedicService(
    @Value("\${SERVICE_MEDIC_API_PATH}") medicApiPath: String,
    private val webClient: WebClient = WebClient.create(medicApiPath)
) {
    private val logger = LoggerFactory.getLogger(MedicService::class.java)

    suspend fun findById(medicId: UUID): Medic? {
        val entity = webClient.get()
            .uri("/$medicId")
            .awaitExchange { clientResponse ->
                clientResponse.awaitEntity<Medic>()
            }
        if (entity.statusCode != HttpStatus.OK) {
            logger.error("Failed to retrieve the medic by ID '$medicId'. Response status: ${entity.statusCodeValue}")
            return null
        }
        return entity.body
    }
}