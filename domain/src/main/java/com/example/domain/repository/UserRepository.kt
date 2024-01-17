package com.example.domain.repository

import com.example.domain.model.User
import com.example.domain.model.UserInfo

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserInfo(userId: String?): UserInfo
}