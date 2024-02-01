package com.example.data.repository

import com.example.core.Response
import com.example.core.safeApiCall
import com.example.data.mapper.user.UserMapper
import com.example.data.mapper.userinfo.UserInfoMapper
import com.example.domain.model.User
import com.example.domain.model.UserInfo
import com.example.domain.repository.UserRepository
import com.example.network.service.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

// Api call to get list of users
class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val userMapper: UserMapper,
    private val userInfoMapper: UserInfoMapper,
) : UserRepository {

    private val tag = javaClass.name
    override suspend fun getUsers(): Flow<Response<List<User>>> =
        flow {
            emit(safeApiCall(tag) { api.getUsers().let(userMapper::map) })
        }.flowOn(ioDispatcher)

    override suspend fun getUserInfo(userId: String?): Flow<Response<UserInfo>> =
        flow {
            emit(safeApiCall(tag) { api.getUserInfo(userId).let(userInfoMapper::map) })
        }.flowOn(ioDispatcher)
}

