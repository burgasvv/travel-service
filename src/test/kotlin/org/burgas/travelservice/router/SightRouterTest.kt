package org.burgas.travelservice.router

import org.burgas.travelservice.dto.city.CityRequest
import org.burgas.travelservice.dto.country.CountryRequest
import org.burgas.travelservice.dto.sight.SightRequest
import org.burgas.travelservice.entity.city.City
import org.burgas.travelservice.entity.country.Country
import org.burgas.travelservice.entity.sight.Sight
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.readValue

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
@TestClassOrder(value = ClassOrderer.OrderAnnotation::class)
@Order(value = 3)
class SightRouterTest(
    @Autowired
    private val mockMvc: MockMvc
) {
    @Test
    @Order(value = 3)
    fun getSights() {
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/sights")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andDo { result -> result.response.setCharacterEncoding("UTF-8") }
            .andDo { result -> println(result.response.contentAsString) }
            .andReturn()
    }

    @Test
    @Order(value = 4)
    fun getSightById() {
        val objectMapper = ObjectMapper()
        val sightsResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/sights")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val sights = objectMapper.readValue<List<Sight>>(sightsResult.response.contentAsString)
        val sight = sights.first { first -> first.name == "Храм Мэйдзи Дост." }
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/sights/by-id")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("sightId", sight.id.toString())
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    @Test
    @Order(value = 1)
    fun createSight() {
        val objectMapper = ObjectMapper()
        val countryRequest = CountryRequest(
            name = "Япония",
            description = "Описание страны Япония"
        )
        val countryString = objectMapper.writeValueAsString(countryRequest)
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/countries/create")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(countryString)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andReturn()
        val countriesResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/countries")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val countries = objectMapper.readValue<List<Country>>(countriesResult.response.contentAsString)
        val country = countries.first { first -> first.name == "Япония" }

        val cityRequest = CityRequest(
            name = "Токио",
            description = "Описание города Токио",
            countryId = country.id
        )
        val cityString = objectMapper.writeValueAsString(cityRequest)
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/cities/create")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(cityString)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andReturn()
        val citiesResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/cities")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val cities = objectMapper.readValue<List<City>>(citiesResult.response.contentAsString)
        val city = cities.first { first -> first.name == "Токио" }

        val sightRequest = SightRequest(
            name = "Храм Мэйдзи",
            description = "Описание достопримечательности Храм Мэйдзи",
            cityId = city.id
        )
        val sightString = objectMapper.writeValueAsString(sightRequest)
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/sights/create")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(sightString)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    @Test
    @Order(value = 2)
    fun updateSight() {
        val objectMapper = ObjectMapper()
        val sightsResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/sights")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val sights = objectMapper.readValue<List<Sight>>(sightsResult.response.contentAsString)
        val sight = sights.first { first -> first.name == "Храм Мэйдзи" }
        val sightRequest = SightRequest(
            id = sight.id,
            name = "Храм Мэйдзи Дост.",
            description = "Описание достопримечательности Храм Мэйдзи Дост.",
        )
        val sightString = objectMapper.writeValueAsString(sightRequest)
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/api/v1/sights/update")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(sightString)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    @Test
    @Order(value = 5)
    fun deleteSight() {
        val objectMapper = ObjectMapper()
        val sightsResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/sights")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val sights = objectMapper.readValue<List<Sight>>(sightsResult.response.contentAsString)
        val sight = sights.first { first -> first.name == "Храм Мэйдзи Дост." }
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/api/v1/sights/delete")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("sightId", sight.id.toString())
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val citiesResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/cities")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val cities = objectMapper.readValue<List<City>>(citiesResult.response.contentAsString)
        val city = cities.first { first -> first.name == "Токио" }
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/api/v1/cities/delete")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("cityId", city.id.toString())
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andReturn()

        val countriesResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/countries")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val countries = objectMapper.readValue<List<Country>>(countriesResult.response.contentAsString)
        val country = countries.first { first -> first.name == "Япония" }
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/api/v1/countries/delete")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("countryId", country.id.toString())
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andReturn()
    }
}