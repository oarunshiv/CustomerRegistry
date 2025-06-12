package rao.vishnu.customerclient.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import rao.vishnu.customerservice.dto.CustomerRequest
import rao.vishnu.customerservice.dto.CustomerResponse

/**
 * A typesafe client for interacting with the Customer service.
 *
 * Provides coroutine-based methods to perform CRUD operations on customer data by making HTTP requests to the
 * configured backend server.
 *
 */
interface CustomerApiClient {
    /**
     * Retrieves all customers from the API.
     * @return a list of [CustomerResponse] objects.
     */
    suspend fun getAll(): List<CustomerResponse>

    /**
     * Retrieves a customer by their unique ID.
     * @param id the customer's ID.
     * @return the [CustomerResponse] for the given ID.
     */
    suspend fun getById(id: String): CustomerResponse

    /**
     * Creates a new customer using the provided data.
     * @return the created [CustomerResponse].
     */
    suspend fun create(
        firstName: String,
        middleName: String?,
        lastName: String,
        email: String,
        phone: String
    ): CustomerResponse

    /**
     * Updates an existing customer with the provided fields.
     * @return the updated [CustomerResponse].
     */
    suspend fun update(
        id: String,
        firstName: String? = null,
        middleName: String? = null,
        lastName: String? = null,
        email: String? = null,
        phone: String? = null,
    ): CustomerResponse

    /**
     * Deletes a customer by their ID.
     * @param id the customer's ID.
     * @return true if the customer was deleted (HTTP 204), false otherwise.
     */
    suspend fun delete(id: String): Boolean

    companion object {
        /**
         * Creates a typesafe client for interacting with Customer Service.
         * @param baseUrl The base URL of the Customer API (e.g., "http://localhost:8080").
         */
        fun create(baseUrl: String): CustomerApiClient = CustomerApiClientImpl(baseUrl)
    }
}

internal class CustomerApiClientImpl(private val baseUrl: String) : CustomerApiClient {
    private val client: HttpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }
        }
    }

    private suspend fun <T> withRetry(block: suspend () -> T): T {
        repeat(3) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                if (attempt == 2) throw e
                // exponential backoff: 200ms, 400ms, 800ms
                delay(2L.shl(attempt) * 100L)
            }
        }
        error("unreachable code")
    }

    private suspend inline fun <reified T> validateAndDeserialize(response: HttpResponse): T {
        if (!response.status.isSuccess()) {
            val errorBody = runCatching { response.bodyAsText() }.getOrDefault("<<no response body>>")
            throw generateApiException(response.status, errorBody)
        }

        return response.body()
    }

    override suspend fun getAll(): List<CustomerResponse> = withRetry {
        validateAndDeserialize(client.get("$baseUrl/customers"))
    }

    override suspend fun getById(id: String): CustomerResponse = withRetry {
        validateAndDeserialize(client.get("$baseUrl/customers/$id"))
    }

    override suspend fun create(
        firstName: String,
        middleName: String?,
        lastName: String,
        email: String,
        phone: String
    ): CustomerResponse = withRetry {
        // TODO validate params
        validateAndDeserialize(client.post("$baseUrl/customers") {
            contentType(ContentType.Application.Json)
            setBody(CustomerRequest(firstName, middleName, lastName, email, phone))
        })
    }

    override suspend fun update(
        id: String,
        firstName: String?,
        middleName: String?,
        lastName: String?,
        email: String?,
        phone: String?,
    ): CustomerResponse = withRetry {
        validateAndDeserialize(client.patch("$baseUrl/customers/$id") {
            contentType(ContentType.Application.Json)
            setBody(CustomerRequest(firstName, middleName, lastName, email, phone))
        })
    }

    override suspend fun delete(id: String): Boolean = withRetry {
        val response = client.delete("$baseUrl/customers/$id")
        response.status == HttpStatusCode.NoContent
    }
}
