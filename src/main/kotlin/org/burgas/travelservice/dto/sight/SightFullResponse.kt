package org.burgas.travelservice.dto.sight

import org.burgas.travelservice.dto.Response
import org.burgas.travelservice.dto.city.CityWithCountryResponse
import org.burgas.travelservice.dto.excursion.ExcursionShortResponse
import java.util.*

data class SightFullResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val city: CityWithCountryResponse?,
    val excursions: List<ExcursionShortResponse>?
) : Response
