package org.burgas.travelservice.dto.sight

import org.burgas.travelservice.dto.Request
import java.util.UUID

data class SightRequest(
    override val id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val cityId: UUID? = null
) : Request
