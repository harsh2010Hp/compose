package com.example.user_feature.presenter.userinfo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.user_feature.presenter.userinfo.state.UserInfoState

@Composable
internal fun UserInfoScreen(userInfoState: UserInfoState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .wrapContentSize(Alignment.Center)
    ) {
        UserInfo(userInfoState.userInfo)
    }
}
