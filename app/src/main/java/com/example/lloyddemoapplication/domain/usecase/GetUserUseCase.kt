package com.example.lloyddemoapplication.domain.usecase

import android.content.Context
import com.example.lloyddemoapplication.R
import com.example.lloyddemoapplication.data.model.User
import com.example.lloyddemoapplication.domain.repository.UserRepository
import com.example.lloyddemoapplication.utils.Response
import com.example.lloyddemoapplication.utils.logs.LoggerUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
    @ApplicationContext private val context: Context
) {
    operator fun invoke(): Flow<Response<List<User>>> = flow {
        handleExceptions(
            getUsers = {
                val users = repository.getUsers()
                LoggerUtils.info("Response is $users")
                emit(Response.Success(users))
            },
            onError = { exception ->
                when (exception) {
                    is HttpException -> Response.Error(context.getString(R.string.default_error))
                    is IOException -> Response.Error(context.getString(R.string.error_internet_connection))
                    else -> Response.Error(context.getString(R.string.unknown_error))
                }
            },
            emitError = { response -> emit(response) }
        )
    }.flowOn(Dispatchers.IO)

    private suspend fun handleExceptions(
        getUsers: suspend () -> Unit,
        onError: (Exception) -> Response<List<User>>,
        emitError: suspend (Response<List<User>>) -> Unit
    ) {
        try {
            getUsers()
        } catch (e: Exception) {
            LoggerUtils.error("Error is ${e.localizedMessage}")
            emitError(onError(e))
        }
    }
}