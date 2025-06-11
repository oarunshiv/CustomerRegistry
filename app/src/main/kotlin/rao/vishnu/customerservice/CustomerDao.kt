package rao.vishnu.customerservice

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.dao.UUIDEntity
import org.jetbrains.exposed.v1.dao.UUIDEntityClass
import java.util.UUID

class CustomerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<CustomerEntity>(Customers)
    var firstName: String by Customers.firstName
    var middleName: String? by Customers.middleName
    var lastName: String by Customers.lastName
    var email: String by Customers.email
    var phone: String by Customers.phone

    fun toCustomer() = Customer(id.toString(), firstName, middleName, lastName, email, phone)
    fun fromCustomer(customer: CreateCustomerRequest) {
        firstName = customer.firstName
        middleName = customer.middleName
        lastName = customer.lastName
        email = customer.email
        phone = customer.phone
    }
}

const val MAX_VARCHAR_LENGTH = 128

object Customers : UUIDTable("customers") {
    val firstName = varchar("first_name", MAX_VARCHAR_LENGTH)
    val middleName = varchar("middle_name", MAX_VARCHAR_LENGTH).nullable()
    val lastName = varchar("last_name", MAX_VARCHAR_LENGTH)
    val email = varchar("email", 255).uniqueIndex()
    val phone = varchar("phone", 15)
}