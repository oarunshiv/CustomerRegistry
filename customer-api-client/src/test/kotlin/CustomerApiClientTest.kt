package rao.vishnu.customerclient.api

import io.ktor.util.collections.ConcurrentSet
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import rao.vishnu.customerservice.dto.CustomerRequest
import rao.vishnu.customerservice.dto.CustomerResponse
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerApiIntegrationTest {

    private lateinit var client: CustomerApiClient
    private val createdCustomers = ConcurrentSet<CustomerResponse>()

    @BeforeAll
    fun setupClient() {
        val baseUrl = System.getProperty("baseUrl") ?: "http://localhost:8080"
        client = CustomerApiClient.create(baseUrl)
    }

    @AfterEach
    fun cleanup() = runBlocking {
        createdCustomers.forEach { customer ->
            runCatching { client.delete(customer.id) }
        }
    }

    private fun randomCustomerRequest(email: String? = null): CustomerRequest {
        val randomSuffix = (1..100).random()
        val emailToUse = email ?: "alice-$randomSuffix@wonderland.com"
        return CustomerRequest(
            "Alice_$randomSuffix",
            "In_$randomSuffix",
            "Wonderland_$randomSuffix",
            emailToUse,
            randomSuffix.toString()
        )
    }

    private suspend fun createCustomer(customer: CustomerRequest = randomCustomerRequest()) = client.create(
        customer.firstName!!,
        customer.middleName,
        customer.lastName!!,
        customer.email!!,
        customer.phone!!
    ).also { createdCustomers.add(it) }

    @Test
    fun `create new customer`() {
        runBlocking {
            val alice = randomCustomerRequest()
            val created = createCustomer(alice)
            assertThat(created)
                .usingRecursiveComparison()
                .ignoringFields("id") // ignores id in the comparison
                .isEqualTo(alice)

            // creating customer with existing emailId fails
            val aliceClone = randomCustomerRequest(alice.email)
            assertFailsWith<ConflictingEmailException> {
                createCustomer(aliceClone)
            }
        }
    }

    @Test
    fun `get all customers`() {
        runBlocking {
            val customers = (1..5).map { createCustomer() }
            val retrieved = client.getAll()
            assertThat(retrieved).containsAll(customers)
        }
    }

    @Test
    fun `get customer by id`() {
        runBlocking {
            val randomCustomer = createCustomer()
            val fetched = client.getById(randomCustomer.id)
            assertEquals(randomCustomer, fetched)

            // get customer by id fails when id is missing
            assertFailsWith<UnknownCustomer> {
                client.getById(UUID.randomUUID().toString())
            }
        }
    }

    @Test
    fun `update customer by id`() {
        runBlocking {
            val randomCustomer = createCustomer()
            val updated = client.update(randomCustomer.id, firstName = "bob_0")
            createdCustomers.remove(randomCustomer)
            createdCustomers.add(updated)
            assertThat(updated)
                .usingRecursiveComparison()
                .ignoringFields("firstName") // ignores id in the comparison
                .isEqualTo(randomCustomer)
            assertEquals("bob_0", updated.firstName)

            // Updating with invalid customerId fails
            assertFailsWith<UnknownCustomer> {
                client.update(UUID.randomUUID().toString())
            }

            // TODO handle conflicting email exception after modifying update to return correct response
            // update customer fails when the update email is already existing

            // val email = "one@mail.com"
            // val randomCustomer2 = createCustomer(randomCustomerRequest(email))

            // assertFailsWith<ConflictingEmailException> {
            //   client.update(randomCustomer2.id, email = randomCustomer.email)
            // }
        }
    }

    @Test
    fun `delete customer by id`() {
        runBlocking {
            val created = createCustomer()
            client.delete(created.id)
            createdCustomers.remove(created)

            assertFailsWith<UnknownCustomer> {
                client.getById(created.id)
            }
        }
    }
}
