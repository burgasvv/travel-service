package org.burgas.travelservice.dto.excursion

import org.burgas.travelservice.dto.Response
import org.burgas.travelservice.dto.sight.SightWithCityResponse
import java.util.*

data class ExcursionFullResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val price: Double?,
    val sights: List<SightWithCityResponse>?
) : Response
