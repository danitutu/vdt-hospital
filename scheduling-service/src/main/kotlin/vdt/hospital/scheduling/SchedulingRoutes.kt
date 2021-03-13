package vdt.hospital.scheduling

import arrow.core.Validated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.mono
import org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder
import org.springdoc.core.fn.builders.operation.Builder
import org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder
import org.springdoc.core.fn.builders.securityrequirement.Builder.securityRequirementBuilder
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*


@Configuration
class SchedulingRoutes(
    private val schedulingService: SchedulingService,
) {

    @Bean
    fun router(): RouterFunction<ServerResponse>? {
        return route()
            .POST("", asHandlerFunction(::createNewAppointment), ::createNewAppointmentApi)
            .build()
    }

    private fun asHandlerFunction(init: suspend (ServerRequest) -> ServerResponse) = HandlerFunction {
        mono(Dispatchers.Unconfined) {
            init(it)
        }
    }

    private fun createNewAppointmentApi(ops: Builder) =
        ops.operationId("createNewAppointment")
            .security(securityRequirementBuilder().name("security-scheme"))
            .summary("Creates a new appointment")
            .requestBody(requestBodyBuilder().implementation(CreateAppointmentDto::class.java))
            .response(
                responseBuilder()
                    .responseCode("200")
                    .description("successful operation")
                    .implementation(Appointment::class.java)
            )
            .response(
                responseBuilder()
                    .responseCode("400")
                    .description("validations failed")
                    .implementation(ValidationErrorDto::class.java)
            )


    private suspend fun createNewAppointment(req: ServerRequest): ServerResponse {
        val body = req.awaitBody<CreateAppointmentDto>()

        return when (val result = schedulingService.createNewAppointment(body)) {
            is Validated.Valid -> ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(result.a)
            is Validated.Invalid -> {
                val list = result.e.map(ValidationErrorDto::fromValidationError)
                ServerResponse.badRequest().bodyValueAndAwait(list)
            }
        }
    }

}
