package rao.vishnu.customerservice

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.Test
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ApplicationTest {
    private fun testLocalApplication(block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
        TestDatabase.init()
        application { appModule() }
        block()
    }

    @Test
    fun testHealthEndpoint() = testLocalApplication {
        val response = client.get("/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains(""""status": "OK""""))
    }

    @Test
    @Order(1)
    fun testCreate() = testLocalApplication {
        val response = client.post("/customers") {
            contentType(ContentType.Application.Json)
            setBody("""{"firstName":"John", "lastName":"Doe","email":"john.doe@example.com","phone":"12345"}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue { response.bodyAsText().isNotEmpty() }
    }

    @Test
    @Order(2)
    fun testGet() = testLocalApplication {
        val response = client.get("/customers")
        assertTrue(response.bodyAsText().contains("John"))
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
