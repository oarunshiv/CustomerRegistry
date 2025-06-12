package rao.vishnu.customerservice

import kotlinx.serialization.Serializable

/**
 * Response DTO representing a customer as returned by the API.
 *
 * @property id Unique identifier for the customer.
 * @property firstName The customer's first name.
 * @property middleName The customer's middle name, if available.
 * @property lastName The customer's last name.
 * @property email The customer's email address.
 * @property phone The customer's phone number.
 */
@Serializable
data class CustomerResponse(
    val id: String,
    val firstName: String,
    val middleName: String? = null,
    val lastName: String,
    val email: String,
    val phone: String
)

/**
 * Request DTO for creating or updating a customer via the API.
 *
 * Note:
 * In `POST /customer`, only [middleName] is nullable. Others should be non-null and not blank.
 * In `PATCH /customer`, non-null properties are set as the new property value. Setting middleName to blank removes any previously set values.
 *
 * @property firstName The customer's first name.
 * @property middleName The customer's middle name.
 * @property lastName The customer's last name.
 * @property email The customer's email address.
 * @property phone The customer's phone number.
 */
@Serializable
data class CustomerRequest(
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null
)

