package org.burgas.travelservice.router

import org.burgas.travelservice.dto.exception.ExceptionResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@Configuration
class SecurityRouter {

    @Bean
    fun securityRoutes() = router {

        "/api/v1/security".nest {
            GET("/csrf-token") { serverRequest ->
                val csrfToken = serverRequest.attribute("_csrf").orElseThrow() as CsrfToken
                ServerResponse.ok().body(csrfToken)
            }
        }

        onError<Exception> { throwable, _ ->
            val exception = ExceptionResponse(
                HttpStatus.BAD_REQUEST.name,
                HttpStatus.BAD_REQUEST.value(),
                throwable.localizedMessage
            )
            ServerResponse.badRequest().body(exception)
        }
    }
}