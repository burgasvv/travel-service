package org.burgas.travelservice.router

import org.burgas.travelservice.dto.identity.IdentityRequest
import org.burgas.travelservice.entity.identity.Authority
import org.burgas.travelservice.entity.identity.Identity
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
@Order(value = 5)
class IdentityRouterTest(
    @Autowired
    private val mockMvc: MockMvc
) {
    @Test
    @Order(value = 2)
    fun getIdentities() {
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/identities")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andDo { result -> result.response.setCharacterEncoding("UTF-8") }
            .andDo { result -> println(result.response.contentAsString) }
            .andReturn()
    }

    @Test
    @Order(value = 3)
    fun getIdentityById() {
        val objectMapper = ObjectMapper()
        val identitiesResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/identities")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andReturn()
        val identities = objectMapper.readValue<List<Identity>>(identitiesResult.response.contentAsString)
        val identity = identities.first { first -> first.email == "user@gmail.com" }
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/identities/by-id")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("identityId", identity.id.toString())
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("user@gmail.com", "user"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andDo { result -> result.response.setCharacterEncoding("UTF-8") }
            .andDo { result -> println(result.response.contentAsString) }
            .andReturn()
    }

    @Test
    @Order(value = 1)
    fun createIdentity() {
        val objectMapper = ObjectMapper()
        val identityRequest = IdentityRequest(
            authority = Authority.USER,
            username = "user",
            password = "user",
            email = "user@gmail.com",
            enabled = true,
            firstname = "User",
            lastname = "User",
            patronymic = "User"
        )
        val identityString = objectMapper.writeValueAsString(identityRequest)
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/identities/create")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(identityString)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    @Test
    @Order(value = 4)
    fun deleteIdentity() {
        val objectMapper = ObjectMapper()
        val identitiesResult = this.mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/identities")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
            )
            .andReturn()
        val identities = objectMapper.readValue<List<Identity>>(identitiesResult.response.contentAsString)
        val identity = identities.first { first -> first.email == "user@gmail.com" }
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/api/v1/identities/delete")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("identityId", identity.id.toString())
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("user@gmail.com", "user"))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }
}