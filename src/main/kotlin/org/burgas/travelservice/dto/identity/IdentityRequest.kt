package org.burgas.travelservice.dto.identity

import org.burgas.travelservice.dto.Request
import org.burgas.travelservice.entity.identity.Authority
import java.util.UUID

data class IdentityRequest(
    override val id: UUID? = null,
    val authority: Authority? = null,
    val username: String? = null,
    val password: String? = null,
    val email: String? = null,
    val enabled: Boolean? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null
) : Request
