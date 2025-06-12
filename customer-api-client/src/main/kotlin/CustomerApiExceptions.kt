package rao.vishnu.customerclient.api

import io.ktor.http.HttpStatusCode

sealed class ApiException(message: String, val statusCode: HttpStatusCode) : Exception(message)

class UnknownCustomer(message: String, statusCode: HttpStatusCode = HttpStatusCode.NotFound) :
    ApiException(message, statusCode)

class InvalidParameterException(message: String, statusCode: HttpStatusCode = HttpStatusCode.BadRequest) :
    ApiException(message, statusCode)

class ConflictingEmailException(message: String, statusCode: HttpStatusCode = HttpStatusCode.Conflict) :
    ApiException(message, statusCode)

class UnknownException(
    message: String,
    statusCode: HttpStatusCode = HttpStatusCode.InternalServerError
) : ApiException(message, statusCode)

class UnexpectedException(message: String, statusCode: HttpStatusCode) : ApiException(message, statusCode)

fun generateApiException(status: HttpStatusCode, body: String): ApiException = when (status) {
    HttpStatusCode.BadRequest -> InvalidParameterException("Bad Request: $body")
    HttpStatusCode.NotFound -> UnknownCustomer("Not Found: $body")
    HttpStatusCode.Conflict -> ConflictingEmailException("Conflict: $body")
    HttpStatusCode.InternalServerError -> UnknownException("Internal Server Error: $body")
    else -> UnexpectedException("Unhandled status ${status.value}: $body", status)
}