package com.example.user_feature.presenter.home.state

import com.example.domain.model.User

data class UserState(
    val users: List<User>? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
)
