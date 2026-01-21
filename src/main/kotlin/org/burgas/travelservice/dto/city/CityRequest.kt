package org.burgas.travelservice.dto.city

import org.burgas.travelservice.dto.Request
import java.util.UUID

data class CityRequest(
    override val id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val countryId: UUID? = null
) : Request