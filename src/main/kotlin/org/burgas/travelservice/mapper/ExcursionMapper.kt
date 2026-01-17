package org.burgas.travelservice.mapper

import org.burgas.travelservice.dto.excursion.ExcursionFullResponse
import org.burgas.travelservice.dto.excursion.ExcursionRequest
import org.burgas.travelservice.dto.excursion.ExcursionShortResponse
import org.burgas.travelservice.entity.excursion.Excursion
import org.burgas.travelservice.repository.ExcursionRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ExcursionMapper : Mapper<ExcursionRequest, Excursion, ExcursionShortResponse, ExcursionFullResponse> {

    final val excursionRepository: ExcursionRepository
    private final val sightMapperObjectFactory: ObjectFactory<SightMapper>

    constructor(excursionRepository: ExcursionRepository, sightMapperObjectFactory: ObjectFactory<SightMapper>) {
        this.excursionRepository = excursionRepository
        this.sightMapperObjectFactory = sightMapperObjectFactory
    }

    private fun getSightMapper(): SightMapper = this.sightMapperObjectFactory.`object`

    override fun toEntity(request: ExcursionRequest): Excursion {
        return this.excursionRepository.findById(request.id ?: UUID.randomUUID())
            .map { excursion ->
                Excursion().apply {
                    this.id = excursion.id
                    this.name = request.name ?: excursion.name
                    this.description = request.description ?: excursion.description
                    this.price = request.price ?: excursion.price
                }
            }
            .orElseGet {
                Excursion().apply {
                    this.name = request.name ?: throw IllegalArgumentException("Excursion name is null")
                    this.description = request.description ?: throw IllegalArgumentException("Excursion description is null")
                    this.price = request.price ?: throw IllegalArgumentException("Excursion price is null")
                }
            }
    }

    override fun toShortResponse(entity: Excursion): ExcursionShortResponse {
        return ExcursionShortResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            price = entity.price
        )
    }

    override fun toFullResponse(entity: Excursion): ExcursionFullResponse {
        return ExcursionFullResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            price = entity.price,
            sights = entity.sights.map { sight -> this.getSightMapper().toSightWithCityResponse(sight) }
        )
    }
}