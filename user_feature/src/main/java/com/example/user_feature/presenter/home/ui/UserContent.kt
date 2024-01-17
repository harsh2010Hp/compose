package com.example.user_feature.presenter.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.core.Route
import com.example.ui.ShowAlert
import com.example.ui.ShowLoader
import com.example.ui.Topbar
import com.example.user_feature.R
import com.example.user_feature.presenter.home.UserViewModel
import com.example.user_feature.presenter.home.effect.UserEffect
import com.example.user_feature.presenter.home.intent.UserIntent


/*
* Calls list of user , shows list of users and update into the view
* On Selection of user, it navigates to UserDetail screen
* */
@Composable
internal fun UserContent(navController: NavHostController) {
    val userViewModel: UserViewModel = hiltViewModel()
    val userState by userViewModel.userState.collectAsState()
    val effect by userViewModel.effect.collectAsState(initial = null)

    // Handle effects
    LaunchedEffect(effect) {
        if (effect is UserEffect.NavigationEffect.NavigateUserInfoScreen) {
            // Navigate to info screen
            navController.navigate(
                Route.UserInfoScreen.route.plus("/")
                    .plus((effect as? UserEffect.NavigationEffect.NavigateUserInfoScreen)?.userId)
            )
        }

    }

    Column {
        Topbar(title = stringResource(R.string.home_screen_title))
        when {
            userState.isLoading -> ShowLoader()
            // Display the error message using a AlertDialog or another UI element
            userState.errorMessage != null -> ShowAlert(userState.errorMessage) {
                userViewModel.processIntent(UserIntent.UIIntent.DialogDismissClicked)
            }

            userState.users.isNullOrEmpty().not() -> UserList(userState.users) {
                userViewModel.processIntent(UserIntent.UIIntent.ListItemClicked(it))
            }

        }
    }
}
