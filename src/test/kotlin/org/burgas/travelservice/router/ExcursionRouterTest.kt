package org.burgas.travelservice.router

import org.burgas.travelservice.dto.excursion.ExcursionRequest
import org.burgas.travelservice.entity.excursion.Excursion
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
@Order(value = 4)
class ExcursionRouterTest(
    @Autowired
    private val mockMvc: MockMvc
) {
    @Test
    @Order(value = 3)
    fun getExcursions() {
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/excursions")
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
    fun getExcursionById() {
        val objectMapper = ObjectMapper()
        val excursionsResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/excursions")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val excursions = objectMapper.readValue<List<Excursion>>(excursionsResult.response.contentAsString)
        val excursion = excursions.first { first -> first.name == "Экскурсия Сапковского Топ" }
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/excursions/by-id")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("excursionId", excursion.id.toString())
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andDo { result -> result.response.setCharacterEncoding("UTF-8") }
            .andDo { result -> println(result.response.contentAsString) }
            .andReturn()
    }

    @Test
    @Order(value = 1)
    fun createExcursion() {
        val objectMapper = ObjectMapper()
        val excursionRequest = ExcursionRequest(
            name = "Экскурсия Сапковского",
            description = "Описание экскурсии Сапковского",
            price = 6800.50
        )
        val excursionString = objectMapper.writeValueAsString(excursionRequest)
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/excursions/create")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(excursionString)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    @Test
    @Order(value = 2)
    fun updateExcursion() {
        val objectMapper = ObjectMapper()
        val excursionsResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/excursions")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val excursions = objectMapper.readValue<List<Excursion>>(excursionsResult.response.contentAsString)
        val excursion = excursions.first { first -> first.name == "Экскурсия Сапковского" }
        val excursionRequest = ExcursionRequest(
            id = excursion.id,
            name = "Экскурсия Сапковского Топ",
            description = "Описание экскурсии Сапковского Топ"
        )
        val excursionString = objectMapper.writeValueAsString(excursionRequest)
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/api/v1/excursions/update")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(excursionString)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    @Test
    @Order(value = 5)
    fun deleteExcursion() {
        val objectMapper = ObjectMapper()
        val excursionsResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/excursions")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        val excursions = objectMapper.readValue<List<Excursion>>(excursionsResult.response.contentAsString)
        val excursion = excursions.first { first -> first.name == "Экскурсия Сапковского Топ" }
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/api/v1/excursions/delete")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("excursionId", excursion.id.toString())
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }
}