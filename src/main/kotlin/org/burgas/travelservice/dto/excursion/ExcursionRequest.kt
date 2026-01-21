package org.burgas.travelservice.dto.excursion

import org.burgas.travelservice.dto.Request
import java.util.UUID

data class ExcursionRequest(
    override val id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null
) : Request
