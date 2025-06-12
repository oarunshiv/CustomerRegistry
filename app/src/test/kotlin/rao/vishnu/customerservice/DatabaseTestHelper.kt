package rao.vishnu.customerservice

import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import rao.vishnu.customerservice.dto.CustomerRequest
import rao.vishnu.customerservice.dto.CustomerResponse

object TestDatabase {
    private val ALICE_CREATE_REQUEST = CustomerRequest("Alice", "In", "Wonderland", "a@example.com", "123")
    private val BOB_CREATE_REQUEST = CustomerRequest("Bob", null, "Builder", "b@example.com", "234")

    lateinit var ALICE: CustomerResponse
    lateinit var BOB: CustomerResponse

    fun init() {
        val dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
        val dbDriver = "org.h2.Driver"
        DatabaseFactory.init(dbUrl, dbDriver, "", "", true)
    }

    fun reset() {
        transaction {
            Customers.deleteAll()
            ALICE = CustomerEntity.new { fromCustomer(ALICE_CREATE_REQUEST) }.toCustomerResponse()
            BOB = CustomerEntity.new { fromCustomer(BOB_CREATE_REQUEST) }.toCustomerResponse()
        }
    }
}