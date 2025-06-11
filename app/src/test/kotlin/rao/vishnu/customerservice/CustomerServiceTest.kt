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
        private val CUSTOMER_ALICE =
            Customer(UUID.randomUUID().toString(), "Alice", "In", "Wonderland", "a@example.com", "123")
        private val ALICE_CREATE_REQUEST = CreateCustomerRequest("Alice", "In", "Wonderland", "a@example.com", "123")
        private val CUSTOMER_BOB =
            Customer(UUID.randomUUID().toString(), "Bob", null, "Builder", "b@example.com", "234")
        private val BOB_CREATE_REQUEST = CreateCustomerRequest("Bob", null, "Builder", "b@example.com", "234")
        private val UPDATE_REQUEST = UpdateCustomerRequest("Thomas", "", "Engine")
    }

    private lateinit var service: CustomerService

    @BeforeTest
    fun setup() {
        service = CustomerService()
    }

    @Test
    fun `should create a customer with UUID`() {
        val created = service.create(ALICE_CREATE_REQUEST)
        assertNotNull(created.id)
        assertEquals(CUSTOMER_ALICE.copy(id = created.id), created)
    }

    @Test
    fun `should return list of all customers`() {
        service.create(ALICE_CREATE_REQUEST)
        service.create(BOB_CREATE_REQUEST)

        val customers = service.getAll()
        assertEquals(2, customers.size)
        assertTrue { customers.all { it.firstName in setOf("Alice", "Bob") } }
    }

    @Test
    fun `should return customer by ID`() {
        val created = service.create(BOB_CREATE_REQUEST)

        val found = service.getById(created.id)
        assertEquals(CUSTOMER_BOB.copy(id = created.id), found)
    }

    @Test
    fun `should return null for non-existent ID`() {
        assertNull(service.getById(UUID.randomUUID().toString()))
    }

    @Test
    fun `should update existing customer`() {
        val created = service.create(ALICE_CREATE_REQUEST)


        val result = service.update(created.id, UPDATE_REQUEST)
        assertTrue(result)

        val fetched = service.getById(created.id)
        val expectedUpdatedCustomer = Customer(
            created.id,
            UPDATE_REQUEST.firstName!!,
            null,
            UPDATE_REQUEST.lastName!!,
            created.email,
            created.phone
        )
        assertEquals(expectedUpdatedCustomer, fetched)
    }

    @Test
    fun `should return false for updating non-existent customer`() {
        val result = service.update(UUID.randomUUID().toString(), UPDATE_REQUEST)
        assertFalse(result)
    }

    @Test
    fun `should delete customer by ID`() {
        val created = service.create(ALICE_CREATE_REQUEST)
        val deleted = service.delete(created.id)
        assertTrue(deleted)
        assertNull(service.getById(created.id))
    }

    @Test
    fun `should return false for deleting non-existent customer`() {
        assertFalse(service.delete(UUID.randomUUID().toString()))
    }
}
