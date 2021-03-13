package vdt.hospital.scheduling

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiDocsConfig {
    @Bean
    fun customOpenAPI(): OpenAPI? {
        val openApi = OpenAPI()
        openApi
            .components(
                Components()
                    .addSecuritySchemes(
                        "security-scheme",
                        SecurityScheme().type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )

        return openApi
    }
}