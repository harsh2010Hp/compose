package com.example.lloyddemoapplication.data.repository

import com.example.lloyddemoapplication.data.model.User
import com.example.lloyddemoapplication.data.remote.network.ApiService
import com.example.lloyddemoapplication.domain.repository.UserRepository
import javax.inject.Inject

// Api call to get list of users
class UserRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserRepository {

    override suspend fun getUsers(): List<User> {
        return api.getUsers()
    }
}