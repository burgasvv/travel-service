package org.burgas.travelservice.mapper

import org.burgas.travelservice.dto.Request
import org.burgas.travelservice.dto.Response
import org.burgas.travelservice.entity.Model
import org.springframework.stereotype.Component

@Component
interface Mapper<in R : Request, M : Model, out S : Response, out F : Response> {

    fun toEntity(request: R): M

    fun toShortResponse(entity: M): S

    fun toFullResponse(entity: M): F
}