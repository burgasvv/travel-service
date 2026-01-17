package org.burgas.travelservice.dto.country

import org.burgas.travelservice.dto.Response
import java.util.UUID

data class CountryShortResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?
) : Response
