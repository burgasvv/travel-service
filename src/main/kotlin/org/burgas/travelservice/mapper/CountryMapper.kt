package org.burgas.travelservice.mapper

import org.burgas.travelservice.dto.country.CountryFullResponse
import org.burgas.travelservice.dto.country.CountryRequest
import org.burgas.travelservice.dto.country.CountryShortResponse
import org.burgas.travelservice.entity.country.Country
import org.burgas.travelservice.repository.CountryRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CountryMapper : Mapper<CountryRequest, Country, CountryShortResponse, CountryFullResponse> {

    final val countryRepository: CountryRepository
    private final val cityMapperObjectFactory: ObjectFactory<CityMapper>

    constructor(countryRepository: CountryRepository, cityMapperObjectFactory: ObjectFactory<CityMapper>) {
        this.countryRepository = countryRepository
        this.cityMapperObjectFactory = cityMapperObjectFactory
    }

    private fun getCityMapper(): CityMapper = this.cityMapperObjectFactory.`object`

    override fun toEntity(request: CountryRequest): Country {
        return this.countryRepository.findById(request.id ?: UUID.randomUUID())
            .map { country ->
                Country().apply {
                    this.id = country.id
                    this.name = request.name ?: country.name
                    this.description = request.description ?: country.description
                }
            }
            .orElseGet {
                Country().apply {
                    this.name = request.name ?: throw IllegalArgumentException("Country name is null")
                    this.description = request.description ?: throw IllegalArgumentException("Country description is null")
                }
            }
    }

    override fun toShortResponse(entity: Country): CountryShortResponse {
        return CountryShortResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description
        )
    }

    override fun toFullResponse(entity: Country): CountryFullResponse {
        return CountryFullResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            cities = entity.cities.map { city -> this.getCityMapper().toShortResponse(city) }
        )
    }
}