package org.burgas.travelservice.dto.city

import org.burgas.travelservice.dto.Request
import java.util.UUID

data class CityRequest(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val countryId: UUID?
) : Request