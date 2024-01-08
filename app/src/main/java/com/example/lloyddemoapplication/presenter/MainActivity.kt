package com.example.lloyddemoapplication.presenter

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lloyddemoapplication.data.model.User
import com.example.lloyddemoapplication.presenter.dashboard.ui.DashboardContentScreen
import com.example.lloyddemoapplication.presenter.home.ui.HomeContentScreen
import com.example.lloyddemoapplication.utils.Route
import com.example.lloyddemoapplication.utils.analytics.Analytics
import com.example.lloyddemoapplication.utils.analytics.AnalyticsImpl
import com.example.lloyddemoapplication.utils.fromJson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Analytics by AnalyticsImpl() {

    private val tag: String = this.javaClass.name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Route.HomeScreen.route) {
                    composable(Route.HomeScreen.route) { HomeContentScreen(navController = navController) }
                    composable(Route.DashboardScreen.route.plus("/{userdetails}"),
                        arguments = listOf(navArgument("userdetails") { type = NavType.StringType }
                        )) {
                        val userJson = it.arguments?.getString("userdetails")
                        val user: User? = userJson?.fromJson()
                        DashboardContentScreen(navController, user)

                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setScreenAnalytics(tag)
    }

    override fun onDestroy() {
        super.onDestroy()
        screenDestroyedAnalytics(tag)
    }
}