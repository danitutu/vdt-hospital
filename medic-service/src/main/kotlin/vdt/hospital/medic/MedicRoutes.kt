package vdt.hospital.medic

import arrow.core.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.mono
import org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder
import org.springdoc.core.fn.builders.operation.Builder
import org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*
import java.util.*

@Configuration
class MedicRoutes(
    private val medicService: MedicService,
) {

    @Bean
    fun router(): RouterFunction<ServerResponse>? {
        return SpringdocRouteBuilder.route()
            .GET("", asHandlerFunction(::search), ::searchApi)
            .POST("", asHandlerFunction(::createNew), ::createNewApi)
            .PUT("/{id}", asHandlerFunction(::update), ::updateApi)
            .GET("/{id}", asHandlerFunction(::findById), ::findByIdApi)
            .build()
    }

    private fun createNewApi(ops: Builder) =
        ops.operationId("createNew")
            .summary("Creates a new medic")
            .requestBody(requestBodyBuilder().implementation(CreateMedicDto::class.java))
            .response(
                responseBuilder()
                    .responseCode("200")
                    .description("successful operation")
                    .implementation(Medic::class.java)
            )
            .response(
                responseBuilder()
                    .responseCode("400")
                    .description("validations failed")
                    .implementation(ValidationErrorDto::class.java)
            )

    private fun updateApi(ops: Builder) =
        ops.operationId("update")
            .summary("Updates medic data")
            .requestBody(requestBodyBuilder().implementation(UpdateMedicDto::class.java))
            .response(
                responseBuilder()
                    .responseCode("200")
                    .description("successful operation")
                    .implementation(Medic::class.java)
            )
            .response(
                responseBuilder()
                    .responseCode("400")
                    .description("validations failed")
                    .implementation(ValidationErrorDto::class.java)
            )

    private fun findByIdApi(ops: Builder) =
        ops.operationId("findById")
            .summary("Searches for medic using ID")
            .requestBody(requestBodyBuilder().implementation(UpdateMedicDto::class.java))
            .response(
                responseBuilder()
                    .responseCode("200")
                    .description("successful operation")
                    .implementation(Medic::class.java)
            )
            .response(
                responseBuilder()
                    .responseCode("404")
                    .description("Medic not found")
                    .implementation(ValidationErrorDto::class.java)
            )

    private fun searchApi(ops: Builder) =
        ops.operationId("search")
            .summary("Searches for medics")
            .response(
                responseBuilder()
                    .responseCode("200")
                    .description("successful operation")
                    .implementationArray(Medic::class.java)
            )

    private fun asHandlerFunction(init: suspend (ServerRequest) -> ServerResponse) = HandlerFunction {
        mono(Dispatchers.Unconfined) {
            init(it)
        }
    }

    private suspend fun createNew(req: ServerRequest): ServerResponse {
        val body = req.awaitBody<CreateMedicDto>()

        return when (val result = medicService.createNew(body)) {
            is Either.Left -> {
                val error = ValidationErrorDto.fromValidationError(result.a)
                ServerResponse.badRequest().bodyValueAndAwait(error)
            }
            is Either.Right -> ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(result.b)
        }
    }

    private suspend fun findById(req: ServerRequest): ServerResponse {
        val id = UUID.fromString(req.pathVariable("id"))

        return medicService.findById(id)?.let { ServerResponse.status(HttpStatus.OK).bodyValueAndAwait(it) }
            ?: ServerResponse.status(HttpStatus.NOT_FOUND).buildAndAwait()
    }

    private suspend fun update(req: ServerRequest): ServerResponse {
        val id = UUID.fromString(req.pathVariable("id"))

        val body = req.awaitBody<UpdateMedicDto>()

        return when (val result = medicService.update(id, body)) {
            is Either.Left -> {
                val error = ValidationErrorDto.fromValidationError(result.a)
                ServerResponse.badRequest().bodyValueAndAwait(error)
            }
            is Either.Right -> ServerResponse.status(HttpStatus.OK).bodyValueAndAwait(result.b)
        }
    }

    private suspend fun search(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyAndAwait(medicService.search())
    }

}
