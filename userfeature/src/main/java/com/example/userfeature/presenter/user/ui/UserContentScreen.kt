package com.example.userfeature.presenter.user.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.ShowAlert
import com.example.ui.ShowLoader
import com.example.ui.Topbar
import com.example.userfeature.R
import com.example.userfeature.presenter.user.UserViewModel
import com.example.userfeature.presenter.user.effect.UserEffect
import com.example.userfeature.presenter.user.intent.UserIntent
import com.example.userfeature.presenter.user.state.UserUIState

@Composable
internal fun UserContentScreen(navigateToUserInfoScreen: (id: String?) -> Unit) {
    val userViewModel: UserViewModel = hiltViewModel()
    val viewState by userViewModel.userState.collectAsState()
    val effect by userViewModel.effect.collectAsState(initial = null)

    HandleEffect(effect, navigateToUserInfoScreen)

    Column {
        Topbar(title = stringResource(R.string.home_screen_title))

        when (viewState) {
            is UserUIState.Loading -> ShowLoader()
            is UserUIState.ShowContent -> {
                val users = (viewState as UserUIState.ShowContent).users
                UserList(users) { user ->
                    userViewModel.processIntent(UserIntent.UIIntent.ListItemClicked(user))
                }
            }

            is UserUIState.Error -> {
                val errorMessage = (viewState as UserUIState.Error).errorMessage
                val showMessage = (viewState as UserUIState.Error).showMessage
                ShowUserErrorDialog(showMessage, errorMessage) {
                    userViewModel.processIntent(UserIntent.UIIntent.DialogDismissClicked)
                }
            }
        }
    }
}

@Composable
private fun HandleEffect(effect: UserEffect?, navigateToUserInfoScreen: (id: String?) -> Unit) {
    when (effect) {
        is UserEffect.NavigationEffect.NavigateUserInfoScreen -> {
            navigateToUserInfoScreen(effect.userId)
        }

        else -> {}
    }
}

@Composable
private fun ShowUserErrorDialog(showMessage: Boolean, errorMessage: Int?, onDismiss: () -> Unit) {
    if (showMessage) {
        ShowAlert(errorMessage, onDismiss)
    }
}
