package org.burgas.travelservice.service

import org.burgas.travelservice.dto.identity.IdentityFullResponse
import org.burgas.travelservice.dto.identity.IdentityRequest
import org.burgas.travelservice.dto.identity.IdentityShortResponse
import org.burgas.travelservice.entity.identity.Identity
import org.burgas.travelservice.mapper.IdentityMapper
import org.springframework.scheduling.annotation.Async
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class IdentityService : AsyncCrudService<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    private final val identityMapper: IdentityMapper
    private final val passwordEncoder: PasswordEncoder

    constructor(identityMapper: IdentityMapper, passwordEncoder: PasswordEncoder) {
        this.identityMapper = identityMapper
        this.passwordEncoder = passwordEncoder
    }

    @Async(value = "taskExecutor")
    override fun findEntity(id: UUID): CompletableFuture<Identity> {
        return CompletableFuture.supplyAsync {
            this.identityMapper.identityRepository.findById(id)
                .orElseThrow { throw IllegalArgumentException("Identity not found") }
        }
    }

    @Async(value = "taskExecutor")
    override fun findById(id: UUID): CompletableFuture<IdentityFullResponse> {
        return CompletableFuture.supplyAsync {
            this.identityMapper.toFullResponse(this.findEntity(id).get())
        }
    }

    @Async(value = "taskExecutor")
    override fun findAll(): CompletableFuture<List<IdentityShortResponse>> {
        return CompletableFuture.supplyAsync {
            this.identityMapper.identityRepository.findAll()
                .map { this.identityMapper.toShortResponse(it) }
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun create(request: IdentityRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val identity = this.identityMapper.toEntity(request)
            this.identityMapper.identityRepository.save(identity)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun update(request: IdentityRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            if (request.id == null) {
                throw IllegalArgumentException("Identity id is null")
            }
            val identity = this.identityMapper.toEntity(request)
            this.identityMapper.identityRepository.save(identity)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val identity = this.findEntity(id).get()
            this.identityMapper.identityRepository.delete(identity)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    fun changePassword(request: IdentityRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            if (request.id == null) {
                throw IllegalArgumentException("Identity id is null")
            }
            if (request.password == null) {
                throw IllegalArgumentException("Identity password is null")
            }
            val identity = this.findEntity(request.id).get()
            if (this.passwordEncoder.matches(request.password, identity.password)) {
                throw IllegalArgumentException("Passwords matched")
            }
            identity.apply {
                this.password = request.password
            }
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    fun changeStatus(request: IdentityRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            if (request.id == null) {
                throw IllegalArgumentException("Identity id is null")
            }
            if (request.enabled == null) {
                throw IllegalArgumentException("Identity enabled is null")
            }
            val identity = this.findEntity(request.id).get()
            if (identity.enabled == request.enabled) {
                throw IllegalArgumentException("Statuses matched")
            }
            identity.apply {
                this.enabled = request.enabled
            }
        }
    }
}