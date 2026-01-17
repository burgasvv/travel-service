package org.burgas.travelservice.dto.excursion

import org.burgas.travelservice.dto.Request
import java.util.UUID

data class ExcursionRequest(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val price: Double?
) : Request
