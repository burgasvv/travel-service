package org.burgas.travelservice.dto.excursion

import org.burgas.travelservice.dto.Response
import java.util.*

data class ExcursionShortResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val price: Double?
) : Response
