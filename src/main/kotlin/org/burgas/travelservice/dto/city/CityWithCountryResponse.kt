package org.burgas.travelservice.dto.city

import org.burgas.travelservice.dto.Response
import org.burgas.travelservice.dto.country.CountryShortResponse
import java.util.UUID

data class CityWithCountryResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val country: CountryShortResponse?
) : Response
