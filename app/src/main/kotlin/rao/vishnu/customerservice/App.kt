package rao.vishnu.customerservice

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.response.respond

fun main() {
    // Create a server and attaching a simple /health route
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) { json() }
        routing {
            get("/health") {
                call.respond(mapOf("status" to "OK"))
            }
        }
    }.start(wait = true)
}
