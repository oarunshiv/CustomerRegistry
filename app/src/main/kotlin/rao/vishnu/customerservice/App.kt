package rao.vishnu.customerservice

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json

val customerService = CustomerService()

/**
 * Configures the main Ktor application module.
 *
 * Installs JSON content negotiation with pretty-printing and lenient parsing.
 * Sets up routing for health checks and customer-related endpoints.
 *
 * @receiver Application The Ktor application instance.
 */
fun Application.appModule() = this.apply {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    routing {
        get("/health") {
            call.respond(mapOf("status" to "OK"))
        }
        customerRoutes(customerService)
    }
}

fun main() {
    DatabaseFactory.init()
    // Create a server and attaching a simple /health route
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        appModule()
    }.start(wait = true)
}
