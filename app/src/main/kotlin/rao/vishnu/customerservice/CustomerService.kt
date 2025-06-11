package rao.vishnu.customerservice

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CustomerService {
    private val customers = ConcurrentHashMap<String, Customer>()

    fun getAll(): List<Customer> = customers.values.toList()

    fun getById(id: String) = customers[id]

    fun create(customer: CreateCustomerRequest): Customer {
        val id = UUID.randomUUID().toString()
        val newCustomer = Customer(
            id = id,
            firstName = customer.firstName,
            middleName = customer.middleName,
            lastName = customer.lastName,
            email = customer.email,
            phone = customer.phone,
        )
        customers[id] = newCustomer

        return newCustomer
    }

    fun update(id: String, updatedCustomer: UpdateCustomerRequest): Boolean {
        return customers.computeIfPresent(id) { id, oldCustomer ->
            val removeMiddleName = updatedCustomer.middleName?.isEmpty() == true
            val updatedMiddleName = if(updatedCustomer.middleName != null) {
                if(removeMiddleName)
                    null
                else
                    updatedCustomer.middleName
            } else {
                oldCustomer.middleName
            }

            Customer(
                id = id,
                firstName = updatedCustomer.firstName ?: oldCustomer.firstName,
                middleName = updatedMiddleName,
                lastName = updatedCustomer.lastName ?: oldCustomer.lastName,
                email = updatedCustomer.email ?: oldCustomer.email,
                phone = updatedCustomer.phone ?: oldCustomer.phone,
            )
        } != null
    }

    fun delete(id: String): Boolean = customers.remove(id) != null
}
