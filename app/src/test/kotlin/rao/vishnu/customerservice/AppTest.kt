package rao.vishnu.customerservice

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ApplicationTest {
    private fun Application.testModule() {
        healthModule()
    }

    @Test
    fun testHealthEndpoint() = testApplication {
        application {
            testModule()
        }

        val response = client.get("/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"status":"OK"}""", response.bodyAsText())
    }
}
