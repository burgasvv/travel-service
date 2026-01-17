package org.burgas.travelservice.repository

import org.burgas.travelservice.entity.excursion.Excursion
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface ExcursionRepository : JpaRepository<Excursion, UUID> {

    @EntityGraph(value = "excursion-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Excursion>
}