package rao.vishnu.customerservice

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID

private val logger = KotlinLogging.logger {}

class CustomerService {
    fun getAll(): List<Customer> = transaction {
        CustomerEntity.all().map { it.toCustomer() }
    }

    fun getById(id: String) = transaction {
        CustomerEntity.findById(UUID.fromString(id))?.toCustomer()
    }

    fun create(customer: CreateCustomerRequest) = try {
        transaction {
            CustomerEntity.new { fromCustomer(customer) }.toCustomer()
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

    fun update(id: String, updatedCustomer: UpdateCustomerRequest): Boolean =
        transaction {
            val updatedEntity = CustomerEntity.findByIdAndUpdate(UUID.fromString(id)) { entity ->
                updatedCustomer.firstName?.let { entity.firstName = it }
                updatedCustomer.lastName?.let { entity.lastName = it }
                updatedCustomer.email?.let { entity.email = it }
                updatedCustomer.phone?.let { entity.phone = it }
                updatedCustomer.middleName?.let {
                    // Remove middleName when empty string is passed explicitly.
                    entity.middleName = it.takeIf { it.isNotBlank() }
                }
            }
            updatedEntity != null
        }

    fun delete(id: String): Boolean = transaction {
        CustomerEntity.findById(UUID.fromString(id))?.delete() != null
    }
}
