package com.example.user_feature.presenter.home.intent


sealed interface UserIntent {
    data object LoadUsers : UserIntent
    sealed interface UIIntent {
        data class ListItemClicked(val userId: String?) : UserIntent
        data object DialogDismissClicked : UserIntent

    }
}
