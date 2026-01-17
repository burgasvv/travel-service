package org.burgas.travelservice.mapper

import org.burgas.travelservice.dto.city.CityFullResponse
import org.burgas.travelservice.dto.city.CityRequest
import org.burgas.travelservice.dto.city.CityShortResponse
import org.burgas.travelservice.dto.city.CityWithCountryResponse
import org.burgas.travelservice.entity.city.City
import org.burgas.travelservice.repository.CityRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class CityMapper : Mapper<CityRequest, City, CityShortResponse, CityFullResponse> {

    final val cityRepository: CityRepository
    private final val countryMapperObjectFactory: ObjectFactory<CountryMapper>
    private final val sightMapperObjectFactory: ObjectFactory<SightMapper>

    constructor(
        cityRepository: CityRepository,
        countryMapperObjectFactory: ObjectFactory<CountryMapper>,
        sightMapperObjectFactory: ObjectFactory<SightMapper>
    ) {
        this.cityRepository = cityRepository
        this.countryMapperObjectFactory = countryMapperObjectFactory
        this.sightMapperObjectFactory = sightMapperObjectFactory
    }

    private fun getCountryMapper(): CountryMapper = this.countryMapperObjectFactory.`object`

    private fun getSightMapper(): SightMapper = this.sightMapperObjectFactory.`object`

    override fun toEntity(request: CityRequest): City {
        return this.cityRepository.findById(request.id ?: UUID.randomUUID())
            .map { city ->
                City().apply {
                    this.id = city.id
                    this.name = request.name ?: city.name
                    this.description = request.description ?: city.description
                    val countryId = request.countryId ?: UUID.randomUUID()
                    val findCountry = getCountryMapper().countryRepository.findById(countryId).orElse(null)
                    this.country = findCountry ?: city.country
                }
            }
            .orElseGet {
                City().apply {
                    this.name = request.name ?: throw IllegalArgumentException("City name is null")
                    this.description = request.description ?: throw IllegalArgumentException("City description is null")
                    val countryId = request.countryId ?: UUID.randomUUID()
                    val findCountry = getCountryMapper().countryRepository.findById(countryId).orElse(null)
                    this.country = findCountry ?: throw IllegalArgumentException("City country is null")
                }
            }
    }

    override fun toShortResponse(entity: City): CityShortResponse {
        return CityShortResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description
        )
    }

    fun toCityWithCountryResponse(entity: City): CityWithCountryResponse {
        return CityWithCountryResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            country = Optional.ofNullable(entity.country)
                .map { country -> this.getCountryMapper().toShortResponse(country) }
                .orElse(null)
        )
    }

    override fun toFullResponse(entity: City): CityFullResponse {
        return CityFullResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            country = Optional.ofNullable(entity.country)
                .map { country -> this.getCountryMapper().toShortResponse(country) }
                .orElse(null),
            sights = entity.sights.map { sight -> this.getSightMapper().toShortResponse(sight) }
        )
    }
}