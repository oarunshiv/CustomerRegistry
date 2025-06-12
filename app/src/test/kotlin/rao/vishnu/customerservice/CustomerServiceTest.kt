package rao.vishnu.customerservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertNull
import rao.vishnu.customerservice.TestDatabase.ALICE
import rao.vishnu.customerservice.TestDatabase.BOB
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {
    companion object {
        private val THOMAS_CREATE_REQUEST = CustomerRequest("Thomas", "Train", "Engine", "tom@example.com", "678")
        private val UPDATE_REQUEST = CustomerRequest("Thomas", "", "Engine")
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
        val created = service.create(THOMAS_CREATE_REQUEST.copy(email = ALICE.email))
        assertNull(created)
    }

    @Test
    fun `should return list of all customers`() {
        val customers = service.getAll()
        assertThat(customers).containsExactlyInAnyOrder(ALICE, BOB)
    }

    @Test
    fun `should return customer by ID`() {
        val found = service.getById(ALICE.id)
        assertEquals(ALICE, found)
    }

    @Test
    fun `should return null for non-existent ID`() {
        assertNull(service.getById(UUID.randomUUID().toString()))
    }

    @Test
    fun `should update existing customer`() {
        val result = service.update(ALICE.id, UPDATE_REQUEST)
        assertNotNull(result)

        val expectedUpdatedCustomerResponse = CustomerResponse(
            ALICE.id,
            UPDATE_REQUEST.firstName!!,
            null,
            UPDATE_REQUEST.lastName!!,
            ALICE.email,
            ALICE.phone
        )
        assertEquals(expectedUpdatedCustomerResponse, result)
    }

    @Test
    fun `should return false for updating non-existent customer`() {
        val result = service.update(UUID.randomUUID().toString(), UPDATE_REQUEST)
        assertNull(result)
    }

    @Test
    fun `should delete customer by ID`() {
        val deleted = service.delete(ALICE.id)
        assertTrue(deleted)
        assertNull(service.getById(ALICE.id))
    }

    @Test
    fun `should return false for deleting non-existent customer`() {
        assertFalse(service.delete(UUID.randomUUID().toString()))
    }
}
