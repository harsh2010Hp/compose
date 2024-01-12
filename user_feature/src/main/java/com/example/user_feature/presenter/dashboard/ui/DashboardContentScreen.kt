package com.example.user_feature.presenter.dashboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.common.ui.Topbar
import com.example.domain.model.User
import com.example.user_feature.R

@Composable
fun DashboardContentScreen(navController: NavController, user: User?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Topbar(
            title = stringResource(R.string.user_detail_screen),
            showBackButton = true,
            onBackPressed = { navController.popBackStack() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .wrapContentSize(Alignment.Center)
        ) {
            UserDetails(user)
        }
    }
}
