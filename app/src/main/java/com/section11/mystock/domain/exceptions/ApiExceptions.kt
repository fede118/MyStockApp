package com.section11.mystock.domain.exceptions

private const val NULL_BODY_ERROR = "Response body is null"
private const val API_ERROR = "API Error:"

/**
 * Exception representing an API error.
 */
class ApiErrorException(
    responseCode: Int,
    errorBody: String?
) : RuntimeException("$API_ERROR $responseCode - ${errorBody ?: NULL_BODY_ERROR}")

/**
 * Exception representing a network error where the body is null.
 */
class ResponseBodyNullException(message: String = NULL_BODY_ERROR) : RuntimeException(message)
