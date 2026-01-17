package org.burgas.travelservice.service

import org.burgas.travelservice.dto.sight.SightFullResponse
import org.burgas.travelservice.dto.sight.SightRequest
import org.burgas.travelservice.dto.sight.SightShortResponse
import org.burgas.travelservice.entity.sight.Sight
import org.burgas.travelservice.mapper.ExcursionMapper
import org.burgas.travelservice.mapper.SightMapper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class SightService : AsyncCrudService<SightRequest, Sight, SightShortResponse, SightFullResponse> {

    private final val sightMapper: SightMapper
    private final val excursionMapper: ExcursionMapper

    constructor(sightMapper: SightMapper, excursionMapper: ExcursionMapper) {
        this.sightMapper = sightMapper
        this.excursionMapper = excursionMapper
    }

    @Async(value = "taskExecutor")
    override fun findEntity(id: UUID): CompletableFuture<Sight> {
        return CompletableFuture.supplyAsync {
            this.sightMapper.sightRepository.findById(id)
                .orElseThrow { throw IllegalArgumentException("Sight not found") }
        }
    }

    @Async(value = "taskExecutor")
    override fun findById(id: UUID): CompletableFuture<SightFullResponse> {
        return CompletableFuture.supplyAsync {
            this.sightMapper.toFullResponse(this.findEntity(id).get())
        }
    }

    @Async(value = "taskExecutor")
    override fun findAll(): CompletableFuture<List<SightShortResponse>> {
        return CompletableFuture.supplyAsync {
            this.sightMapper.sightRepository.findAll()
                .map { sight -> this.sightMapper.toShortResponse(sight) }
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun create(request: SightRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val sight = this.sightMapper.toEntity(request)
            this.sightMapper.sightRepository.save(sight)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun update(request: SightRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            if (request.id == null) {
                throw IllegalArgumentException("Sight id is null")
            }
            val sight = this.sightMapper.toEntity(request)
            this.sightMapper.sightRepository.save(sight)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val sight = this.findEntity(id).get()
            this.sightMapper.sightRepository.delete(sight)
        }
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    fun addExcursion(sightId: UUID, excursionId: UUID) {
        val sight = this.sightMapper.sightRepository.findById(sightId)
            .orElseThrow { throw IllegalArgumentException("Sight not found") }
        val excursion = this.excursionMapper.excursionRepository.findById(excursionId)
            .orElseThrow { throw IllegalArgumentException("Excursion not found") }
        if (sight.excursions.map { excursion -> excursion.id }.toList().contains(excursion.id)) {
            throw IllegalArgumentException("Excursion for sight already added")
        }
        sight.addExcursion(excursion)
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    fun removeExcursion(sightId: UUID, excursionId: UUID) {
        val sight = this.sightMapper.sightRepository.findById(sightId)
            .orElseThrow { throw IllegalArgumentException("Sight not found") }
        val excursion = this.excursionMapper.excursionRepository.findById(excursionId)
            .orElseThrow { throw IllegalArgumentException("Excursion not found") }
        sight.removeExcursion(excursion)
    }
}