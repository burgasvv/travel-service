package org.burgas.travelservice.dto.sight

import org.burgas.travelservice.dto.Response
import org.burgas.travelservice.dto.city.CityWithCountryResponse
import java.util.*

data class SightWithCityResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val city: CityWithCountryResponse?
) : Response
