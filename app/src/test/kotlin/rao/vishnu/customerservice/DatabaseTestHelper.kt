package rao.vishnu.customerservice

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object TestDatabase {
    private val ALICE_CREATE_REQUEST = CreateCustomerRequest("Alice", "In", "Wonderland", "a@example.com", "123")
    private val BOB_CREATE_REQUEST = CreateCustomerRequest("Bob", null, "Builder", "b@example.com", "234")

    lateinit var ALICE_CUSTOMER: Customer
    lateinit var BOB_CUSTOMER: Customer

    fun init() {
        Database.connect("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Customers)
        }
    }

    fun reset() {
        transaction {
            Customers.deleteAll()
            ALICE_CUSTOMER = CustomerEntity.new { fromCustomer(ALICE_CREATE_REQUEST) }.toCustomer()
            BOB_CUSTOMER = CustomerEntity.new { fromCustomer(BOB_CREATE_REQUEST) }.toCustomer()
        }
    }
}