package com.example.user_feature.presenter.userinfo.effect

sealed interface UserInfoEffect {

    sealed interface BackPressEffect : UserInfoEffect {
        data object BackPressed : BackPressEffect
    }
}