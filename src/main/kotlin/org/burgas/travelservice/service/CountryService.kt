package org.burgas.travelservice.service

import org.burgas.travelservice.dto.country.CountryFullResponse
import org.burgas.travelservice.dto.country.CountryRequest
import org.burgas.travelservice.dto.country.CountryShortResponse
import org.burgas.travelservice.entity.country.Country
import org.burgas.travelservice.mapper.CountryMapper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class CountryService : AsyncCrudService<CountryRequest, Country, CountryShortResponse, CountryFullResponse> {

    private final val countryMapper: CountryMapper

    constructor(countryMapper: CountryMapper) {
        this.countryMapper = countryMapper
    }

    @Async(value = "taskExecutor")
    override fun findEntity(id: UUID): CompletableFuture<Country> {
        return CompletableFuture.supplyAsync {
            this.countryMapper.countryRepository.findById(id)
                .orElseThrow { throw IllegalArgumentException("Country not found") }
        }
    }

    @Async(value = "taskExecutor")
    override fun findById(id: UUID): CompletableFuture<CountryFullResponse> {
        return CompletableFuture.supplyAsync {
            this.countryMapper.toFullResponse(this.findEntity(id).get())
        }
    }

    @Async(value = "taskExecutor")
    override fun findAll(): CompletableFuture<List<CountryShortResponse>> {
        return CompletableFuture.supplyAsync {
            this.countryMapper.countryRepository.findAll()
                .map { country -> this.countryMapper.toShortResponse(country) }
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun create(request: CountryRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val country = this.countryMapper.toEntity(request)
            this.countryMapper.countryRepository.save(country)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun update(request: CountryRequest): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            if (request.id == null) {
                throw IllegalArgumentException("Country id is null")
            }
            val country = this.countryMapper.toEntity(request)
            this.countryMapper.countryRepository.save(country)
        }
    }

    @Async(value = "taskExecutor")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class, Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val country = this.findEntity(id).get()
            this.countryMapper.countryRepository.delete(country)
        }
    }
}