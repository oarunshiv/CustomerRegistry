package rao.vishnu.customerservice

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import rao.vishnu.customerservice.dto.CustomerRequest
import rao.vishnu.customerservice.dto.CustomerResponse
import java.util.UUID

private val logger = KotlinLogging.logger {}

/**
 * Service layer for managing customer data and business logic.
 *
 * Provides methods for basic CRUD operations on customers, interacting with the database
 * via Exposed ORM. Handles transaction management and error handling for database operations.
 *
 * All methods run within a database transaction.
 */
class CustomerService {
    /**
    * Retrieves all customers from the database.
    * @return a list of [CustomerResponse] objects.
    */
    fun getAll(): List<CustomerResponse> = transaction {
        CustomerEntity.all().map { it.toCustomerResponse() }
    }

    /**
     * Retrieves a customer by their unique ID.
     * @param id the UUID of the customer as a string.
     * @return the [CustomerResponse] if found, or null if not found.
     */
    fun getById(id: String) = transaction {
        CustomerEntity.findById(UUID.fromString(id))?.toCustomerResponse()
    }

    /**
     * Creates a new customer in the database.
     * Handles duplicate email constraint violations.
     *
     * @param createRequest the customer data to create.
     * @return the created [CustomerResponse], or null if the email is duplicate or any other error occurs.
     */
    fun create(createRequest: CustomerRequest) = try {
        transaction {
            CustomerEntity.new { fromCustomer(createRequest) }.toCustomerResponse()
        }
    } catch (e: ExposedSQLException) {
        if(e.errorCode == 23505) {
            logger.error(e) { "emailId is duplicate" }
        } else {
            logger.error(e) { "Unknown exception" }
        }
        // TODO instead of returning null, return proper data type with possible return values
        null
    }

    /**
     * Updates an existing customer with the provided data.
     * Supports partial updates; only non-null fields are updated.
     *
     * @param id the UUID of the customer as a string.
     * @param updateRequest the fields to update.
     * @return the updated [CustomerResponse], or null if the customer is not found.
     */
    fun update(id: String, updateRequest: CustomerRequest) =
        transaction {
            CustomerEntity.findByIdAndUpdate(UUID.fromString(id)) { entity ->
                updateRequest.firstName?.let { entity.firstName = it }
                updateRequest.lastName?.let { entity.lastName = it }
                updateRequest.email?.let { entity.email = it }
                updateRequest.phone?.let { entity.phone = it }
                updateRequest.middleName?.let {
                    // Remove middleName when empty string is passed explicitly.
                    entity.middleName = it.takeIf { it.isNotBlank() }
                }
            }?.toCustomerResponse()
        }

    /**
     * Deletes a customer by ID.
     *
     * @param id the UUID of the customer as a string.
     * @return true if the customer was deleted, false if not found.
     */
    fun delete(id: String): Boolean = transaction {
        CustomerEntity.findById(UUID.fromString(id))?.delete() != null
    }
}
