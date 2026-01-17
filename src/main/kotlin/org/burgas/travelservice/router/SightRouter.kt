package org.burgas.travelservice.router

import org.burgas.travelservice.dto.exception.ExceptionResponse
import org.burgas.travelservice.dto.sight.SightRequest
import org.burgas.travelservice.service.SightService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import java.util.*

@Configuration
class SightRouter {

    private final val sightService: SightService

    constructor(sightService: SightService) {
        this.sightService = sightService
    }

    @Bean
    fun sightRoutes() = router {

        "/api/v1/sights".nest {

            GET("") { _ ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sightService.findAll().get())
            }

            GET("/by-id") { serverRequest ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        sightService.findById(UUID.fromString(
                            serverRequest.param("sightId").orElseThrow()
                        ))
                            .get()
                    )
            }

            POST("/create") { serverRequest ->
                sightService.create(serverRequest.body<SightRequest>()).get()
                ServerResponse.ok().build()
            }

            PUT("/update") { serverRequest ->
                sightService.update(serverRequest.body<SightRequest>()).get()
                ServerResponse.ok().build()
            }

            DELETE("/delete") { serverRequest ->
                sightService.delete(UUID.fromString(serverRequest.param("sightId").orElseThrow())).get()
                ServerResponse.ok().build()
            }

            POST("/add-excursion") { serverRequest ->
                sightService.addExcursion(
                    UUID.fromString(serverRequest.param("sightId").orElseThrow()),
                    UUID.fromString(serverRequest.param("excursionId").orElseThrow())
                )
                ServerResponse.ok().build()
            }

            DELETE("/remove-excursion") { serverRequest ->
                sightService.removeExcursion(
                    UUID.fromString(serverRequest.param("sightId").orElseThrow()),
                    UUID.fromString(serverRequest.param("excursionId").orElseThrow())
                )
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