package org.burgas.travelservice.dto.sight

import org.burgas.travelservice.dto.Response
import java.util.UUID

data class SightShortResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?
) : Response
