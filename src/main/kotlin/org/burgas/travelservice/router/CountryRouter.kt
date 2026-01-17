package org.burgas.travelservice.router

import org.burgas.travelservice.dto.country.CountryRequest
import org.burgas.travelservice.dto.exception.ExceptionResponse
import org.burgas.travelservice.service.CountryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import java.util.UUID

@Configuration
class CountryRouter {

    private final val countryService: CountryService

    constructor(countryService: CountryService) {
        this.countryService = countryService
    }

    @Bean
    fun countryRoutes() = router {

        "/api/v1/countries".nest {

            GET("") { _ ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(countryService.findAll().get())
            }

            GET("/by-id") { serverRequest ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        countryService.findById(UUID.fromString(
                            serverRequest.param("countryId").orElseThrow())
                        ).get()
                    )
            }

            POST("/create") { serverRequest ->
                countryService.create(serverRequest.body<CountryRequest>()).get()
                ServerResponse.ok().build()
            }

            PUT("/update") { serverRequest ->
                countryService.update(serverRequest.body<CountryRequest>()).get()
                ServerResponse.ok().build()
            }

            DELETE("/delete") { serverRequest ->
                countryService.delete(UUID.fromString(serverRequest.param("countryId").orElseThrow())).get()
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