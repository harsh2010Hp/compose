package com.example.user_feature.presenter.userinfo.state

import com.example.domain.model.UserInfo

data class UserInfoState(
    val userInfo: UserInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
)