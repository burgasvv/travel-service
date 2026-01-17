package org.burgas.travelservice.dto.country

import org.burgas.travelservice.dto.Request
import java.util.UUID

data class CountryRequest(
    override val id: UUID?,
    val name: String?,
    val description: String?
) : Request
