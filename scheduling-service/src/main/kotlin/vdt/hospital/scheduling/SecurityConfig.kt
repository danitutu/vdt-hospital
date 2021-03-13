package vdt.hospital.scheduling

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        //@formatter:off
        return http
            .csrf().disable()
            .authorizeExchange()
                .pathMatchers(
                    HttpMethod.GET,
                    "/swagger-ui.html",
                    "/webjars/swagger-ui/index.html",
                    "/v3/api-docs/swagger-config",
                    "/v3/api-docs",
                ).permitAll()
                .anyExchange().authenticated()
                .and()
            .oauth2ResourceServer()
                .jwt()
                .and()
            .and()
            .build()
        //@formatter:on
//http://localhost:9822/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/default/createNewAppointment
    }
}