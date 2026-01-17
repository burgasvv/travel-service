package org.burgas.travelservice.router

import org.burgas.travelservice.dto.city.CityRequest
import org.burgas.travelservice.dto.exception.ExceptionResponse
import org.burgas.travelservice.service.CityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import java.util.UUID

@Configuration
class CityRouter {

    private final val cityService: CityService

    constructor(cityService: CityService) {
        this.cityService = cityService
    }

    @Bean
    fun cityRoutes() = router {

        "/api/v1/cities".nest {

            GET("") { _ ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cityService.findAll().get())
            }

            GET("/by-id") { serverRequest ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        cityService.findById(UUID.fromString(
                            serverRequest.param("cityId").orElseThrow()
                        )).get()
                    )
            }

            POST("/create") { serverRequest ->
                cityService.create(serverRequest.body<CityRequest>()).get()
                ServerResponse.ok().build()
            }

            PUT("/update") { serverRequest ->
                cityService.update(serverRequest.body<CityRequest>()).get()
                ServerResponse.ok().build()
            }

            DELETE("/delete") { serverRequest ->
                cityService.delete(UUID.fromString(serverRequest.param("cityId").orElseThrow())).get()
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