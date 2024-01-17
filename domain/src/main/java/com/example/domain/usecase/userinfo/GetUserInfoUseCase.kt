package com.example.domain.usecase.userinfo

import com.example.core.Response
import com.example.core.logs.LoggerDelegateProvider
import com.example.domain.model.UserInfo
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val logger by LoggerDelegateProvider()
    operator fun invoke(userId: String?): Flow<Response<UserInfo>> = flow {
        try {
            val users = repository.getUserInfo(userId)
            logger.info("Response is $users")
            emit(Response.Success(users))
        } catch (exception: Throwable) {
            logger.error("Error is ${exception.localizedMessage}")
            emit(Response.Error(exception = exception))
        }
    }.flowOn(ioDispatcher)
}