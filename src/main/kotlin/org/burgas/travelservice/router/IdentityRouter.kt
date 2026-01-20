package org.burgas.travelservice.router

import org.burgas.travelservice.dto.identity.IdentityRequest
import org.burgas.travelservice.service.IdentityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import java.util.*

@Configuration
class IdentityRouter {

    private final val identityService: IdentityService

    constructor(identityService: IdentityService) {
        this.identityService = identityService
    }

    @Bean
    fun identityRoutes() = router {

        "/api/v1/identities".nest {
            GET("") { _ ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(identityService.findAll().get())
            }

            GET("/by-id") { serverRequest ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        identityService.findById(
                            UUID.fromString(serverRequest.param("identityId").orElseThrow())
                        )
                            .get()
                    )
            }

            POST("/create") { serverRequest ->
                identityService.create(serverRequest.body<IdentityRequest>()).get()
                ServerResponse.ok().build()
            }

            PUT("/update") { serverRequest ->
                identityService.update(serverRequest.body<IdentityRequest>()).get()
                ServerResponse.ok().build()
            }

            DELETE("/delete") { serverRequest ->
                identityService.delete(UUID.fromString(serverRequest.param("identityId").orElseThrow())).get()
                ServerResponse.ok().build()
            }

            PUT("/change-password") { serverRequest ->
                identityService.changePassword(serverRequest.body<IdentityRequest>()).get()
                ServerResponse.ok().build()
            }

            PUT("/change-status") { serverRequest ->
                identityService.changeStatus(serverRequest.body<IdentityRequest>()).get()
                ServerResponse.ok().build()
            }
        }

//        onError<Exception> { throwable, _ ->
//            val exception = ExceptionResponse(
//                HttpStatus.BAD_REQUEST.name,
//                HttpStatus.BAD_REQUEST.value(),
//                throwable.localizedMessage
//            )
//            ServerResponse.badRequest().body(exception)
//        }
    }
}