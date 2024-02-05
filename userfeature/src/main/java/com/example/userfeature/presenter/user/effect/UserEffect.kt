package com.example.userfeature.presenter.user.effect

sealed interface UserEffect {
    data class ShowErrorDialog(val errorMessage: Int, val onDismiss: () -> Unit) : UserEffect
    sealed class NavigationEffect : UserEffect {
        data class NavigateUserInfoScreen(val userId: String?) : NavigationEffect()
    }
}