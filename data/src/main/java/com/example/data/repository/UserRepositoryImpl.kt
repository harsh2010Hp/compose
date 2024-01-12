package com.example.data.repository

import com.example.data.mapper.toUser
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.network.service.ApiService
import javax.inject.Inject

// Api call to get list of users
class UserRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserRepository {

    override suspend fun getUsers(): List<User> {
        return api.getUsers().toUser()
    }
}

