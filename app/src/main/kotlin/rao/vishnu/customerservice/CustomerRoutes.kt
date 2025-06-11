package rao.vishnu.customerservice

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

private val logger = KotlinLogging.logger {}

fun Route.customerRoutes(customerService: CustomerService) {
    route("/customers") {
        get {
            call.respond(customerService.getAll())
        }

        get("{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid ID")
                return@get
            }

            val customer = customerService.getById(id)
            if (customer == null) {
                call.respond(HttpStatusCode.NotFound, "Customer not found")
            } else {
                call.respond(customer)
            }
        }

        post {
            val customer = try {
                call.receive<Customer>()
            } catch (e: ContentTransformationException) {
                val body = call.request.call.pipelineCall
                logger.error(e) { "Error transforming $body" }
                call.respond(HttpStatusCode.BadRequest, "Invalid input params: $e")
                return@post
            }
            val created = customerService.create(customer)
            call.respond(HttpStatusCode.Created, created)
        }

        put("{id}") {
            val id = call.parameters["id"]
            val customer = call.receive<Customer>()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val updated = customerService.update(id, customer)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Customer not found")
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val deleted = customerService.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Customer not found")
            }
        }
    }
}
