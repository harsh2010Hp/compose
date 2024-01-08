package com.example.lloyddemoapplication.data.remote.network

import com.example.lloyddemoapplication.data.model.User
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>
}