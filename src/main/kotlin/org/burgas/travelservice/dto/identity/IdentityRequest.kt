package org.burgas.travelservice.dto.identity

import org.burgas.travelservice.dto.Request
import org.burgas.travelservice.entity.identity.Authority
import java.util.UUID

data class IdentityRequest(
    override val id: UUID?,
    val authority: Authority?,
    val username: String?,
    val password: String?,
    val email: String?,
    val enabled: Boolean?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?
) : Request
