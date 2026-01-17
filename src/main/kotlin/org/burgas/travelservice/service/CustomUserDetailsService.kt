package org.burgas.travelservice.service

import org.burgas.travelservice.entity.identity.IdentityDetails
import org.burgas.travelservice.repository.IdentityRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    private final val identityRepository: IdentityRepository

    constructor(identityRepository: IdentityRepository) {
        this.identityRepository = identityRepository
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return IdentityDetails(
            this.identityRepository.findIdentityByEmail(username)
                .orElseThrow { throw IllegalArgumentException("Identity not found") }
        )
    }
}