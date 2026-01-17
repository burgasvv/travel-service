package org.burgas.travelservice.dto.country

import org.burgas.travelservice.dto.Response
import org.burgas.travelservice.dto.city.CityShortResponse
import java.util.UUID

data class CountryFullResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val cities: List<CityShortResponse>?
) : Response
