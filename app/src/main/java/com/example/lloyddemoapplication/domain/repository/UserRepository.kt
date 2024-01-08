package com.example.lloyddemoapplication.domain.repository

import com.example.lloyddemoapplication.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}