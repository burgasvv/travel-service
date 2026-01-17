package org.burgas.travelservice.router

import org.burgas.travelservice.dto.exception.ExceptionResponse
import org.burgas.travelservice.dto.excursion.ExcursionRequest
import org.burgas.travelservice.service.ExcursionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import java.util.*

@Configuration
class ExcursionRouter {

    private final val excursionService: ExcursionService

    constructor(excursionService: ExcursionService) {
        this.excursionService = excursionService
    }

    @Bean
    fun excursionRoutes() = router {

        "/api/v1/excursions".nest {

            GET("") { _ ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(excursionService.findAll().get())
            }

            GET("/by-id") { serverRequest ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        excursionService.findById(UUID.fromString(
                            serverRequest.param("excursionId").orElseThrow()
                        )).get()
                    )
            }

            POST("/create") { serverRequest ->
                excursionService.create(serverRequest.body<ExcursionRequest>()).get()
                ServerResponse.ok().build()
            }

            PUT("/update") { serverRequest ->
                excursionService.update(serverRequest.body<ExcursionRequest>()).get()
                ServerResponse.ok().build()
            }

            DELETE("/delete") { serverRequest ->
                excursionService.delete(UUID.fromString(serverRequest.param("excursionId").orElseThrow())).get()
                ServerResponse.ok().build()
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