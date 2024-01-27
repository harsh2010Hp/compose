package com.example.core.exception

import com.example.core.exception.enum.ErrorType

fun Throwable.handleExceptions(): Int {
    return ErrorType.fromThrowable(this).errorMessageId
}
