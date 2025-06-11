package rao.vishnu.customerservice

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.assertNull
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CustomerServiceTest {

    companion object {
        private val CUSTOMER_ALICE = Customer(null, "Alice", "Wonderland", "a@example.com", "123", "In")
        private val CUSTOMER_BOB = Customer(null, "Bob", "Builder", "b@example.com", "234")

    }
    private lateinit var service: CustomerService

    @BeforeTest
    fun setup() {
        service = CustomerService()
    }

    @Test
    fun `should create a customer with UUID`() {
        val created = service.create(CUSTOMER_ALICE)
        assertNotNull(created.id)
        assertEquals(CUSTOMER_ALICE.copy(id = created.id), created)
    }

    @Test
    fun `should return list of all customers`() {
        service.create(CUSTOMER_ALICE)
        service.create(CUSTOMER_BOB)

        val customers = service.getAll()
        assertEquals(2, customers.size)
        assertTrue { customers.all { it.firstName in setOf("Alice", "Bob") } }
    }

    @Test
    fun `should return customer by ID`() {
        val created = service.create(CUSTOMER_BOB)

        val found = service.getById(created.id!!)
        assertEquals(CUSTOMER_BOB.copy(id = created.id), found)
    }

    @Test
    fun `should return null for non-existent ID`() {
        assertNull(service.getById(UUID.randomUUID().toString()))
    }

    @Test
    fun `should update existing customer`() {
        val created = service.create(CUSTOMER_BOB)

        val updated = Customer(null, "Thomas", "Engine", "tom@example.com", "555")
        val result = service.update(created.id!!, updated)
        assertTrue(result)

        val fetched = service.getById(created.id)
        assertEquals(updated.copy(id = created.id), fetched)
    }

    @Test
    fun `should return false for updating non-existent customer`() {
        val result = service.update(UUID.randomUUID().toString(), CUSTOMER_ALICE)
        assertFalse(result)
    }

    @Test
    fun `should delete customer by ID`() {
        val created = service.create(CUSTOMER_ALICE)
        val deleted = service.delete(created.id!!)
        assertTrue(deleted)
        assertNull(service.getById(created.id))
    }

    @Test
    fun `should return false for deleting non-existent customer`() {
        assertFalse(service.delete(UUID.randomUUID().toString()))
    }
}
