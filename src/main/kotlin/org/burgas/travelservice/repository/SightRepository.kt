package org.burgas.travelservice.repository

import org.burgas.travelservice.entity.sight.Sight
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface SightRepository : JpaRepository<Sight, UUID> {

    @EntityGraph(value = "sight-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Sight>
}