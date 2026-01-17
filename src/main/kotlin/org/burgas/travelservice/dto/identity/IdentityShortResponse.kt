package org.burgas.travelservice.dto.identity

import org.burgas.travelservice.dto.Response
import java.util.UUID

data class IdentityShortResponse(
    override val id: UUID?,
    val username: String?,
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?
) : Response
