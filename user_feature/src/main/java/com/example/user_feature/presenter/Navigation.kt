package com.example.user_feature.presenter

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.common.Route
import com.example.common.fromJson
import com.example.domain.model.User
import com.example.user_feature.presenter.dashboard.ui.DashboardContentScreen
import com.example.user_feature.presenter.home.ui.HomeContentScreen

@Composable
fun UserFlowNavigation() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Route.HomeScreen.route) {
            composable(Route.HomeScreen.route) { HomeContentScreen(navController = navController) }
            composable(Route.DashboardScreen.route.plus("/{userdetails}"),
                arguments = listOf(navArgument("userdetails") { type = NavType.StringType })
            ) {
                val userJson = it.arguments?.getString("userdetails")
                val user: User? = userJson?.fromJson()
                DashboardContentScreen(navController, user)
            }
        }
    }
}