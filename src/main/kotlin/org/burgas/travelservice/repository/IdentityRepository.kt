package org.burgas.travelservice.repository

import org.burgas.travelservice.entity.identity.Identity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface IdentityRepository : JpaRepository<Identity, UUID> {

    fun findIdentityByEmail(email: String): Optional<Identity>
}