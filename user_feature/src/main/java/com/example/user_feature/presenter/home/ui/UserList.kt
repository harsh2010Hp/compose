package com.example.user_feature.presenter.home.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.example.domain.model.User

// To show User List
@Composable
internal fun UserList(users: List<User>?, onItemClicked: (String?) -> Unit) {
    LazyColumn {
        items(count = users?.size ?: 0) { index ->
            val user = users?.get(index)
            UserItem(user) {
                onItemClicked(user?.id)
            }
        }
    }
}