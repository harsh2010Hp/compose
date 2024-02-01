package com.example.userfeature.presenter.user.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.core.Route
import com.example.ui.ShowAlert
import com.example.ui.ShowLoader
import com.example.ui.Topbar
import com.example.userfeature.R
import com.example.userfeature.presenter.user.UserViewModel
import com.example.userfeature.presenter.user.effect.UserEffect
import com.example.userfeature.presenter.user.intent.UserIntent
import com.example.userfeature.presenter.user.state.UserUIState

@Composable
internal fun UserContent(navController: NavHostController) {
    val userViewModel: UserViewModel = hiltViewModel()
    val userState by userViewModel.userState.collectAsState()
    val effect by userViewModel.effect.collectAsState(initial = null)

    HandleUserEffect(effect, navController)

    Column {
        Topbar(title = stringResource(R.string.home_screen_title))

        when (userState) {
            is UserUIState.Loading -> ShowLoader()
            is UserUIState.ShowContent -> {
                val users = (userState as UserUIState.ShowContent).users
                UserList(users) { user ->
                    userViewModel.processIntent(UserIntent.UIIntent.ListItemClicked(user))
                }
            }

            is UserUIState.Error -> {
                val errorMessage = (userState as UserUIState.Error).errorMessage
                val showMessage = (userState as UserUIState.Error).showMessage
                ShowUserErrorDialog(showMessage, errorMessage) {
                    userViewModel.processIntent(UserIntent.UIIntent.DialogDismissClicked)
                }
            }
        }
    }
}

@Composable
private fun HandleUserEffect(effect: UserEffect?, navController: NavHostController) {
    if (effect is UserEffect.NavigationEffect.NavigateUserInfoScreen) {
        navController.navigate(
            Route.UserInfoScreen.route.plus("/")
                .plus((effect as? UserEffect.NavigationEffect.NavigateUserInfoScreen)?.userId)
        )
    }
}

@Composable
private fun ShowUserErrorDialog(showMessage: Boolean, errorMessage: Int?, onDismiss: () -> Unit) {
    if (showMessage) {
        ShowAlert(errorMessage, onDismiss)
    }
}
