package com.example.user_feature.presenter

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.Route
import com.example.user_feature.presenter.home.ui.UserContentScreen
import com.example.user_feature.presenter.userinfo.ui.UserInfoContentScreen

@Composable
fun UserFlowNavigation() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Route.HomeScreen.route) {
            composable(Route.HomeScreen.route) { UserContentScreen(navController = navController) }
            composable(
                Route.UserInfoScreen.route.plus("/{userid}"),
                arguments = listOf(navArgument("userid") { type = NavType.StringType })
            ) {
                UserInfoContentScreen(navController)
            }
        }
    }
}