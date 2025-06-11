package rao.vishnu.customerservice

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val middleName: String? = null
)
