package org.burgas.travelservice.service

import org.burgas.travelservice.dto.city.CityFullResponse
import org.burgas.travelservice.dto.city.CityRequest
import org.burgas.travelservice.dto.city.CityShortResponse
import org.burgas.travelservice.entity.city.City
import org.burgas.travelservice.mapper.CityMapper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class CityService : AsyncCrudService<CityRequest, City, CityShortResponse, CityFullResponse> {

    private final val cityMapper: CityMapper

    constructor(cityMapper: CityMapper) {
        this.cityMapper = cityMapper
    }

    @Async(value = "taskExecutor")
    override fun findEntity(id: UUID): CompletableFuture<City> {
        return CompletableFuture.supplyAsync {
            this.cityMapper.cityRepository.findById(id)
                .orElseThrow { throw IllegalArgumentException("City not found") }
        }
    }

    @Async(value = "taskExecutor")
    override fun findById(id: UUID): CompletableFuture<CityFullResponse> {
        return CompletableFuture.supplyAsync {
            this.cityMapper.toFullResponse(this.findEntity(id).get())
        }
    }

    @Async(value = "taskExecutor")
    override fun findAll(): CompletableFuture<List<CityShortResponse>> {
        return CompletableFuture.supplyAsync {
            this.cityMapper.cityRepository.findAll()
                .map { city -> this.cityMapper.toShortResponse(city) }
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun create(request: CityRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val city = this.cityMapper.toEntity(request)
            this.cityMapper.cityRepository.save(city)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun update(request: CityRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            if (request.id == null) {
                throw IllegalArgumentException("City id is null")
            }
            val city = this.cityMapper.toEntity(request)
            this.cityMapper.cityRepository.save(city)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val city = this.findEntity(id).get()
            this.cityMapper.cityRepository.delete(city)
        }
    }
}