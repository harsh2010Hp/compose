package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository

open class TestUserRepository : UserRepository {
    override suspend fun getUsers(): List<User> {
        // Return a list of mock users
        return listOf(
            User(name = "User 1", email = "user1@example.com"),
            User(name = "User 2", email = "user2@example.com"),
        )
    }
}