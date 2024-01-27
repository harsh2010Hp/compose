package com.example.core

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Response<T> {
    return try {
        Response.Success(apiCall())
    } catch (e: Exception) {
        Response.Error(e)
    }
}