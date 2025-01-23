package com.section11.mystock.framework.networking

import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import retrofit2.Response

suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> Response<T>
): T {
    val response = apiCall()
    if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            return body
        } else {
            throw ResponseBodyNullException()
        }
    } else {
        throw ApiErrorException(response.code(), response.errorBody()?.string())
    }
}
