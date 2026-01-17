package org.burgas.travelservice.mapper

import org.burgas.travelservice.dto.identity.IdentityFullResponse
import org.burgas.travelservice.dto.identity.IdentityRequest
import org.burgas.travelservice.dto.identity.IdentityShortResponse
import org.burgas.travelservice.entity.identity.Identity
import org.burgas.travelservice.repository.IdentityRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class IdentityMapper : Mapper<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    final val identityRepository: IdentityRepository
    private final val passwordEncoder: PasswordEncoder

    constructor(identityRepository: IdentityRepository, passwordEncoder: PasswordEncoder) {
        this.identityRepository = identityRepository
        this.passwordEncoder = passwordEncoder
    }

    override fun toEntity(request: IdentityRequest): Identity = this.identityRepository.findById(request.id ?: UUID.randomUUID())
        .map {
            identity -> Identity().apply {
                this.id = identity.id
                this.authority = request.authority ?: identity.authority
                this.username = request.username ?: identity.username
                this.password = identity.password
                this.email = request.email ?: identity.email
                this.enabled = request.enabled ?: identity.enabled
                this.firstname = request.firstname ?: identity.firstname
                this.lastname = request.lastname ?: identity.lastname
                this.patronymic = request.patronymic ?: identity.patronymic
            }
        }
        .orElseGet {
            Identity().apply {
                val newPassword = request.password ?: throw IllegalArgumentException("Identity password is null")
                this.authority = request.authority ?: throw IllegalArgumentException("Identity authority is null")
                this.username = request.username ?: throw IllegalArgumentException("Identity username is null")
                this.password = passwordEncoder.encode(newPassword) ?: throw IllegalArgumentException("Password not encoded")
                this.email = request.email ?: throw IllegalArgumentException("Identity email is null")
                this.enabled = request.enabled ?: throw IllegalArgumentException("Identity enabled is null")
                this.firstname = request.firstname ?: throw IllegalArgumentException("Identity firstname is null")
                this.lastname = request.lastname ?: throw IllegalArgumentException("Identity lastname is null")
                this.patronymic = request.patronymic ?: throw IllegalArgumentException("Identity patronymic is null")
            }
        }

    override fun toShortResponse(entity: Identity): IdentityShortResponse {
        return IdentityShortResponse(
            id = entity.id,
            username = entity.username,
            email = entity.email,
            firstname = entity.firstname,
            lastname = entity.lastname,
            patronymic = entity.patronymic
        )
    }

    override fun toFullResponse(entity: Identity): IdentityFullResponse {
        return IdentityFullResponse(
            id = entity.id,
            username = entity.username,
            email = entity.email,
            firstname = entity.firstname,
            lastname = entity.lastname,
            patronymic = entity.patronymic
        )
    }
}