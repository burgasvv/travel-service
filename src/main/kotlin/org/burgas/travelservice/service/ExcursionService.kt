package org.burgas.travelservice.service

import org.burgas.travelservice.dto.excursion.ExcursionFullResponse
import org.burgas.travelservice.dto.excursion.ExcursionRequest
import org.burgas.travelservice.dto.excursion.ExcursionShortResponse
import org.burgas.travelservice.entity.excursion.Excursion
import org.burgas.travelservice.mapper.ExcursionMapper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class ExcursionService : AsyncCrudService<ExcursionRequest, Excursion, ExcursionShortResponse, ExcursionFullResponse> {

    private final val excursionMapper: ExcursionMapper

    constructor(excursionMapper: ExcursionMapper) {
        this.excursionMapper = excursionMapper
    }

    @Async(value = "taskExecutor")
    override fun findEntity(id: UUID): CompletableFuture<Excursion> {
        return CompletableFuture.supplyAsync {
            this.excursionMapper.excursionRepository.findById(id)
                .orElseThrow { throw IllegalArgumentException("Excursion not found") }
        }
    }

    @Async(value = "taskExecutor")
    override fun findById(id: UUID ): CompletableFuture<ExcursionFullResponse> {
        return CompletableFuture.supplyAsync {
            this.excursionMapper.toFullResponse(this.findEntity(id).get())
        }
    }

    @Async(value = "taskExecutor")
    override fun findAll(): CompletableFuture<List<ExcursionShortResponse>> {
        return CompletableFuture.supplyAsync {
            this.excursionMapper.excursionRepository.findAll()
                .map { excursion -> this.excursionMapper.toShortResponse(excursion) }
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun create(request: ExcursionRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val excursion = this.excursionMapper.toEntity(request)
            this.excursionMapper.excursionRepository.save(excursion)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun update(request: ExcursionRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            if (request.id == null) {
                throw IllegalArgumentException("Excursion id is null")
            }
            val excursion = this.excursionMapper.toEntity(request)
            this.excursionMapper.excursionRepository.save(excursion)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val excursion = this.findEntity(id).get()
            this.excursionMapper.excursionRepository.delete(excursion)
        }
    }
}