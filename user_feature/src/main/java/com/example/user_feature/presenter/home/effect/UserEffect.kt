package com.example.user_feature.presenter.home.effect

sealed interface UserEffect {
    sealed class NavigationEffect : UserEffect {
        data class NavigateUserInfoScreen(val userId: String?) : NavigationEffect()
    }

}