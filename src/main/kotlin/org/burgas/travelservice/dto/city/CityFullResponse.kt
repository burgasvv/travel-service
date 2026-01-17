package org.burgas.travelservice.dto.city

import org.burgas.travelservice.dto.Response
import org.burgas.travelservice.dto.country.CountryShortResponse
import org.burgas.travelservice.dto.sight.SightShortResponse
import org.burgas.travelservice.dto.sight.SightWithCityResponse
import java.util.UUID

data class CityFullResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val country: CountryShortResponse?,
    val sights: List<SightShortResponse>?
) : Response
