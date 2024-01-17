package com.example.core

import java.lang.Exception

sealed class Response<out T>(val data: T? = null, val exception: Throwable? = null) {
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(exception: Throwable) : Response<T>(exception = exception)
}