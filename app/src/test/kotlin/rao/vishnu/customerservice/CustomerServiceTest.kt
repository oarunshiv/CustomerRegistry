package rao.vishnu.customerservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertNull
import rao.vishnu.customerservice.TestDatabase.ALICE_CUSTOMER
import rao.vishnu.customerservice.TestDatabase.BOB_CUSTOMER
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {
    companion object {
        private val THOMAS_CREATE_REQUEST = CreateCustomerRequest("Thomas", "Train", "Engine", "tom@example.com", "678")
        private val UPDATE_REQUEST = UpdateCustomerRequest("Thomas", "", "Engine")
    }

    private lateinit var service: CustomerService

    @BeforeAll
    fun setupDatabase() {
        TestDatabase.init()
    }


    @BeforeTest
    fun setup() {
        TestDatabase.reset()
        service = CustomerService()
    }

    @Test
    fun `should create and retrieve a customer`() {
        val created = service.create(THOMAS_CREATE_REQUEST)!!
        assertNotNull(created.id)
        assertThat(THOMAS_CREATE_REQUEST)
            .usingRecursiveComparison()
            .ignoringFields("id") // ignores id in the comparison
            .isEqualTo(created)
    }

    @Test
    fun `should fail when creating customer with existing email`() {
        val created = service.create(THOMAS_CREATE_REQUEST.copy(email = ALICE_CUSTOMER.email))
        assertNull(created)
    }

    @Test
    fun `should return list of all customers`() {
        val customers = service.getAll()
        assertThat(customers).containsExactlyInAnyOrder(ALICE_CUSTOMER, BOB_CUSTOMER)
    }

    @Test
    fun `should return customer by ID`() {
        val found = service.getById(ALICE_CUSTOMER.id)
        assertEquals(ALICE_CUSTOMER, found)
    }

    @Test
    fun `should return null for non-existent ID`() {
        assertNull(service.getById(UUID.randomUUID().toString()))
    }

    @Test
    fun `should update existing customer`() {
        val result = service.update(ALICE_CUSTOMER.id, UPDATE_REQUEST)
        assertTrue(result)

        val fetched = service.getById(ALICE_CUSTOMER.id)
        val expectedUpdatedCustomer = Customer(
            ALICE_CUSTOMER.id,
            UPDATE_REQUEST.firstName!!,
            null,
            UPDATE_REQUEST.lastName!!,
            ALICE_CUSTOMER.email,
            ALICE_CUSTOMER.phone
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
        val deleted = service.delete(ALICE_CUSTOMER.id)
        assertTrue(deleted)
        assertNull(service.getById(ALICE_CUSTOMER.id))
    }

    @Test
    fun `should return false for deleting non-existent customer`() {
        assertFalse(service.delete(UUID.randomUUID().toString()))
    }
}
