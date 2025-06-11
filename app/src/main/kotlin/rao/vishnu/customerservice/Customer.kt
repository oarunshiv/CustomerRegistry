package rao.vishnu.customerservice

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: String,
    val firstName: String,
    val middleName: String? = null,
    val lastName: String,
    val email: String,
    val phone: String
)

@Serializable
data class CreateCustomerRequest(
    val firstName: String,
    val middleName: String? = null,
    val lastName: String,
    val email: String,
    val phone: String
)

@Serializable
data class UpdateCustomerRequest(
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null
)

