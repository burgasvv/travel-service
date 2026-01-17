package org.burgas.travelservice.dto.sight

import org.burgas.travelservice.dto.Request
import java.util.UUID

data class SightRequest(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val cityId: UUID?
) : Request
