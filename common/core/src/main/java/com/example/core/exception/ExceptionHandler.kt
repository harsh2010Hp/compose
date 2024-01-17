package com.example.core.exception

import android.net.http.HttpException
import com.example.core.exception.enum.ErrorType
import java.io.IOException

fun Throwable.handleExceptions(): Int {
    return ErrorType.fromThrowable(this).errorMessageId
}
