package rao.vishnu.customerservice

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.response.respond
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

fun Application.testModule() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/health") {
            call.respond(mapOf("status" to "OK"))
        }
    }
}

class ApplicationTest {
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
