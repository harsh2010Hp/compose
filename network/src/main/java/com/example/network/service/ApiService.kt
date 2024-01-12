package com.example.network.service

import com.example.network.model.UserResponse
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<UserResponse>
}