package rao.vishnu.customerservice

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import rao.vishnu.customerservice.dto.CustomerRequest

private val logger = KotlinLogging.logger {}

/**
 * Defines RESTful customer-related API routes for the application.
 *
 * Registers endpoints under the `/customers` path for CRUD operations:
 *
 * - **GET `/customers`**: Returns a list of all customers.
 * - **GET `/customers/{id}`**: Returns the customer with the specified ID, or 404 if not found.
 * - **POST `/customers`**: Creates a new customer from the request body. Validates required fields and checks for duplicate emails.
 * - **PATCH `/customers/{id}`**: Updates an existing customer with the specified ID using the provided fields. Returns 404 if not found.
 * - **DELETE `/customers/{id}`**: Deletes the customer with the specified ID. Returns 204 on success or 404 if not found.
 *
 * Input validation and error handling are performed for all endpoints, with appropriate HTTP status codes.
 *
 * @receiver Route The Ktor routing context to which the customer routes are added.
 * @param customerService The service layer handling business logic for customer operations.
 */
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
                call.receive<CustomerRequest>()
            } catch (e: ContentTransformationException) {
                val body = call.request.call.pipelineCall
                logger.error(e) { "Error transforming $body" }
                call.respond(HttpStatusCode.BadRequest, "Invalid input params: $e")
                return@post
            }
            val invalidFields = buildSet {
                if (customer.firstName.isNullOrBlank()) add("firstName")
                if (customer.lastName.isNullOrBlank()) add("lastName")
                if (customer.email.isNullOrBlank()) add("email")
                if (customer.phone.isNullOrBlank()) add("phone")
            }
            if(invalidFields.isNotEmpty()) {
                logger.warn { "$invalidFields have null or empty values." }
                call.respond(HttpStatusCode.BadRequest, "Invalid input params: $invalidFields")
                return@post
            }

            val created = customerService.create(customer)
            if(created == null) {
                call.respond(HttpStatusCode.BadRequest, "Duplicate emailId")
                return@post
            }
            call.respond(HttpStatusCode.Created, created)
        }

        patch("{id}") {
            val id = call.parameters["id"]
            val customer = call.receive<CustomerRequest>()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@patch
            }

            val updated = customerService.update(id, customer)
            if (updated == null) {
                call.respond(HttpStatusCode.NotFound, "Customer not found")
            } else {
                call.respond(HttpStatusCode.OK, updated)
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
