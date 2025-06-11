package rao.vishnu.customerservice

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CustomerService {
    private val customers = ConcurrentHashMap<String, Customer>()

    fun getAll(): List<Customer> = customers.values.toList()

    fun getById(id: String) = customers[id]

    fun create(customer: Customer): Customer {
        val id = UUID.randomUUID().toString()
        val newCustomer = customer.copy(id = id)
        customers[id] = newCustomer

        return newCustomer
    }

    fun update(id: String, updatedCustomer: Customer): Boolean {
        return customers.computeIfPresent(id) { id, oldCustomer -> updatedCustomer.copy(id = id) } != null
    }

    fun delete(id: String): Boolean = customers.remove(id) != null
}
