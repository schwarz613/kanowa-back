package com.kanowa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    properties = [
        "spring.datasource.url=jdbc:h2:mem:kanowadb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
    ]
)
class KanowaApiIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun fullCrudAndDashboardFlowWorksWithout500() {
        // 1. Register user
        val registerBody = """{"name":"Teste","email":"teste@kanowa.com","password":"password123"}"""
        val registerRes = mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerBody)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andReturn()

        val userId = objectMapper.readTree(registerRes.response.contentAsString).get("id").asLong()

        // 2. Login
        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"teste@kanowa.com","password":"password123"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.user.id").value(userId))

        // 3. Create Credit
        val creditRes = mockMvc.perform(
            post("/api/credits")
                .header("X-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Salario","amount":5000.0,"frequency":"Mensal"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.userId").value(userId))
            .andReturn()

        val creditId = objectMapper.readTree(creditRes.response.contentAsString).get("id").asText()

        // 4. Read, Update Credit
        mockMvc.perform(get("/api/credits").header("X-User-Id", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(creditId))

        mockMvc.perform(
            put("/api/credits/$creditId")
                .header("X-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Salario Atualizado","amount":5500.0}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Salario Atualizado"))
            .andExpect(jsonPath("$.amount").value(5500.0))

        // 5. Create, Read, Update Debit
        val debitRes = mockMvc.perform(
            post("/api/debits")
                .header("X-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Aluguel","amount":1500.0,"category":"Aluguel"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.userId").value(userId))
            .andReturn()

        val debitId = objectMapper.readTree(debitRes.response.contentAsString).get("id").asText()

        mockMvc.perform(get("/api/debits").header("X-User-Id", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(debitId))

        mockMvc.perform(
            put("/api/debits/$debitId")
                .header("X-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"status":"PAGO"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("PAGO"))

        // 6. Dashboard Summary & Transactions (both with and without X-User-Id)
        mockMvc.perform(get("/api/dashboard/summary").header("X-User-Id", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalBalance").value(4000.0))

        mockMvc.perform(get("/api/dashboard/transactions").header("X-User-Id", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))

        mockMvc.perform(get("/api/credits"))
            .andExpect(status().isOk)

        // 7. Delete Credit & Debit
        mockMvc.perform(delete("/api/credits/$creditId").header("X-User-Id", userId))
            .andExpect(status().isNoContent)

        mockMvc.perform(delete("/api/debits/$debitId").header("X-User-Id", userId))
            .andExpect(status().isNoContent)
    }
}
