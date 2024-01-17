package com.example.user_feature.presenter.userinfo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui.ShowAlert
import com.example.ui.ShowLoader
import com.example.ui.Topbar
import com.example.user_feature.R
import com.example.user_feature.presenter.userinfo.UserInfoViewModel
import com.example.user_feature.presenter.userinfo.effect.UserInfoEffect
import com.example.user_feature.presenter.userinfo.intent.UserInfoIntent

@Composable
internal fun UserInfoContentScreen(navController: NavController) {

    val userInfoViewModel = hiltViewModel<UserInfoViewModel>()
    val userInfoState by userInfoViewModel.userInfoState.collectAsState()
    val effect by userInfoViewModel.effect.collectAsState(initial = null)

    LaunchedEffect(key1 = effect) {
        if (effect is UserInfoEffect.BackPressEffect.BackPressed) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Topbar(
            title = stringResource(R.string.user_detail_screen),
            showBackButton = true,
            onBackPressed = { userInfoViewModel.processIntent(UserInfoIntent.BackPressed.BackPressClicked) })
        when {
            userInfoState.isLoading -> {
                // To handle Loader
                ShowLoader()
            }

            userInfoState.errorMessage != null -> {
                // To handle what to do with error Message
                ShowAlert(errorMessage = userInfoState.errorMessage) {
                    userInfoViewModel.processIntent(UserInfoIntent.UIIntent.DismissErrorDialog)
                }
            }

            userInfoState.userInfo != null -> {
                // To load user info into ui
                UserInfoScreen(userInfoState)
            }
        }
    }
}


