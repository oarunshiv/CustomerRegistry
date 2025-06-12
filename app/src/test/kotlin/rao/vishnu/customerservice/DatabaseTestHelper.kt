package rao.vishnu.customerservice

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object TestDatabase {
    private val ALICE_CREATE_REQUEST = CustomerRequest("Alice", "In", "Wonderland", "a@example.com", "123")
    private val BOB_CREATE_REQUEST = CustomerRequest("Bob", null, "Builder", "b@example.com", "234")

    lateinit var ALICE: CustomerResponse
    lateinit var BOB: CustomerResponse

    fun init() {
        Database.connect("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Customers)
        }
    }

    fun reset() {
        transaction {
            Customers.deleteAll()
            ALICE = CustomerEntity.new { fromCustomer(ALICE_CREATE_REQUEST) }.toCustomerResponse()
            BOB = CustomerEntity.new { fromCustomer(BOB_CREATE_REQUEST) }.toCustomerResponse()
        }
    }
}