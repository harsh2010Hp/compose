package com.example.domain.repository

import com.example.core.Response
import com.example.domain.model.User
import com.example.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): Flow<Response<List<User>>>
    suspend fun getUserInfo(userId: String?): Flow<Response<UserInfo>>
}