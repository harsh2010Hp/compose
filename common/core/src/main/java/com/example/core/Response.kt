package com.example.core

sealed class Response<out T>(
    val data: T? = null,
    val exception: Throwable? = null,
    val showLoading: Boolean = false
) {
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(exception: Throwable) : Response<T>(exception = exception)
    class Loading<T>(showLoading: Boolean) : Response<T>(showLoading = showLoading)
}