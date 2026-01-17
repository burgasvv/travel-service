package org.burgas.travelservice.mapper

import org.burgas.travelservice.dto.sight.SightFullResponse
import org.burgas.travelservice.dto.sight.SightRequest
import org.burgas.travelservice.dto.sight.SightShortResponse
import org.burgas.travelservice.dto.sight.SightWithCityResponse
import org.burgas.travelservice.entity.sight.Sight
import org.burgas.travelservice.repository.SightRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class SightMapper : Mapper<SightRequest, Sight, SightShortResponse, SightFullResponse> {

    final val sightRepository: SightRepository
    private final val cityMapperObjectFactory: ObjectFactory<CityMapper>
    private final val excursionMapperObjectFactory: ObjectFactory<ExcursionMapper>

    constructor(
        sightRepository: SightRepository,
        cityMapperObjectFactory: ObjectFactory<CityMapper>,
        excursionMapperObjectFactory: ObjectFactory<ExcursionMapper>
    ) {
        this.sightRepository = sightRepository
        this.cityMapperObjectFactory = cityMapperObjectFactory
        this.excursionMapperObjectFactory = excursionMapperObjectFactory
    }

    private fun getCityMapper(): CityMapper = this.cityMapperObjectFactory.`object`

    private fun getExcursionMapper(): ExcursionMapper = this.excursionMapperObjectFactory.`object`

    override fun toEntity(request: SightRequest): Sight {
        return this.sightRepository.findById(request.id ?: UUID.randomUUID())
            .map { sight ->
                Sight().apply {
                    this.id = sight.id
                    this.name = request.name ?: sight.name
                    this.description = request.description ?: sight.description
                    val cityId = request.cityId ?: UUID.randomUUID()
                    val findCity = getCityMapper().cityRepository.findById(cityId).orElse(null)
                    this.city = findCity ?: sight.city
                }
            }
            .orElseGet {
                Sight().apply {
                    this.name = request.name ?: throw IllegalArgumentException("Sight name is null")
                    this.description = request.description ?: throw IllegalArgumentException("Sight description is null")
                    val cityId = request.cityId ?: UUID.randomUUID()
                    val findCity = getCityMapper().cityRepository.findById(cityId).orElse(null)
                    this.city = findCity ?: throw IllegalArgumentException("Sight city is null")
                }
            }
    }

    override fun toShortResponse(entity: Sight): SightShortResponse {
        return SightShortResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description
        )
    }

    fun toSightWithCityResponse(entity: Sight): SightWithCityResponse {
        return SightWithCityResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            city = Optional.ofNullable(entity.city)
                .map { city -> this.getCityMapper().toCityWithCountryResponse(city) }
                .orElse(null)
        )
    }

    override fun toFullResponse(entity: Sight): SightFullResponse {
        return SightFullResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            city = Optional.ofNullable(entity.city)
                .map { city -> this.getCityMapper().toCityWithCountryResponse(city) }
                .orElse(null),
            excursions = entity.excursions.map { excursion -> this.getExcursionMapper().toShortResponse(excursion) }
        )
    }
}