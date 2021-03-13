package vdt.hospital.patient

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.core.codec.DecodingException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

@Component
@Order(-2)
class ErrorHandler(
    errorAttributes: ErrorAttributes, resources: Resources,
    applicationContext: ApplicationContext, serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {
    init {
        super.setMessageWriters(serverCodecConfigurer.writers);
        super.setMessageReaders(serverCodecConfigurer.readers);
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), createResponse(errorAttributes))
    }

    private fun createResponse(errorAttributes: ErrorAttributes?): (request: ServerRequest) -> Mono<ServerResponse> =
        {
            // errorAttributes.getErrorAttributes(it, ErrorAttributeOptions.defaults())
            when (val error = errorAttributes!!.getError(it)) {
                is ServerWebInputException -> handleServerWebInputException(error)
                is AppException -> handleAppException(error)
                else -> handle(error)
            }
        }

    private fun handleServerWebInputException(error: Throwable) = if (error.cause == null) handle(error)
    else {
        when ((error.cause)) {
            is DecodingException -> {
                val decodingException = error.cause
                when ((decodingException)?.cause) {
                    is MissingKotlinParameterException -> handleAppException(ValidationException("Missing parameter '${((decodingException.cause) as MissingKotlinParameterException).parameter.name}'"))
                    else -> handle(error)
                }
            }
            else -> handle(error)
        }
    }

    private fun handle(error: Throwable?): Mono<ServerResponse> {
        logger.warn("Error occurred", error)
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                mapOf("message" to "An error occurred during the request")
            )
    }

    private fun handleAppException(error: AppException) =
        ServerResponse.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                mapOf(
                    "type" to error.type,
                    "message" to error.message
                )
            )

    companion object {
        private val logger = LoggerFactory.getLogger(ErrorHandler::class.java)
    }
}