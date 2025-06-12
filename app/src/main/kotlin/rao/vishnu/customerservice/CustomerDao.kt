package rao.vishnu.customerservice

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.dao.UUIDEntity
import org.jetbrains.exposed.v1.dao.UUIDEntityClass
import rao.vishnu.customerservice.dto.CustomerRequest
import rao.vishnu.customerservice.dto.CustomerResponse
import java.util.UUID

/**
 * Exposed entity class representing a row in the 'customers' table.
 *
 * Maps customer data fields and provides conversion methods between
 * database entities and API DTOs.
 *
 * @property firstName The customer's first name.
 * @property middleName The customer's middle name, nullable.
 * @property lastName The customer's last name.
 * @property email The customer's email address.
 * @property phone The customer's phone number.
 */
class CustomerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<CustomerEntity>(Customers)
    var firstName: String by Customers.firstName
    var middleName: String? by Customers.middleName
    var lastName: String by Customers.lastName
    var email: String by Customers.email
    var phone: String by Customers.phone

    /**
     * Converts this entity to a [CustomerResponse] DTO.
     */
    fun toCustomerResponse() = CustomerResponse(id.toString(), firstName, middleName, lastName, email, phone)

    /**
     * Updates this entity's fields from a [CustomerRequest] DTO.
     *
     * Assumes all required fields in [CustomerRequest] are non-null.
     */
    fun fromCustomer(customer: CustomerRequest) {
        firstName = customer.firstName!!
        middleName = customer.middleName
        lastName = customer.lastName!!
        email = customer.email!!
        phone = customer.phone!!
    }
}

/**
 * Maximum length for VARCHAR fields in customer-related tables.
 */
const val MAX_VARCHAR_LENGTH = 128

/**
 * Exposed table definition for the 'customers' table.
 */
object Customers : UUIDTable("customers") {
    val firstName = varchar("first_name", MAX_VARCHAR_LENGTH)
    val middleName = varchar("middle_name", MAX_VARCHAR_LENGTH).nullable()
    val lastName = varchar("last_name", MAX_VARCHAR_LENGTH)
    val email = varchar("email", 255).uniqueIndex()
    val phone = varchar("phone", 15)
}