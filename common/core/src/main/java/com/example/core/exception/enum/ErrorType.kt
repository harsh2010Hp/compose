package com.example.core.exception.enum

import android.net.http.HttpException
import com.example.core.R
import java.io.IOException

internal enum class ErrorType(val errorMessageId: Int) {
    DEFAULT_ERROR(R.string.default_error),
    INTERNET_CONNECTION_ERROR(R.string.error_internet_connection),
    UNKNOWN_ERROR(R.string.unknown_error);

    companion object {
        fun fromThrowable(throwable: Throwable): ErrorType {
            return when (throwable) {
                is HttpException -> DEFAULT_ERROR
                is IOException -> INTERNET_CONNECTION_ERROR
                else -> UNKNOWN_ERROR
            }
        }
    }
}