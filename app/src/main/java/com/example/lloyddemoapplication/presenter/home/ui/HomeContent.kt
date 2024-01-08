package com.example.lloyddemoapplication.presenter.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lloyddemoapplication.R
import com.example.lloyddemoapplication.data.model.User
import com.example.lloyddemoapplication.presenter.home.HomeViewModel
import com.example.lloyddemoapplication.utils.Route
import com.example.lloyddemoapplication.utils.toJson
import com.example.lloyddemoapplication.utils.ui.ShowLoader
import com.example.lloyddemoapplication.utils.ui.SimpleAlertDialog
import com.example.lloyddemoapplication.utils.ui.Topbar


/*
* Calls list of user , shows list of users and update into the view
* On Selection of user, it navigates to UserDetail screen
* */
@Composable
fun HomeContent(navController: NavHostController) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val users = homeViewModel.users.collectAsState(null)
    val loader = homeViewModel.loading.collectAsState(initial = false)
    val showDialog = homeViewModel.showDialog.collectAsState(initial = false)

    LaunchedEffect(Unit) {
        homeViewModel.getUsers()
    }
    if (loader.value) {
        ShowLoader()
    }
    MaterialTheme {
        Surface {
            Column {
                Topbar(title = stringResource(R.string.home_screen_title))
                UserList(users.value?.data ?: listOf(), navController)
            }
        }
    }

    // Display the error message using a AlertDialog or another UI element
    if (showDialog.value) {
        ShowAlert(homeViewModel)
    }
}

// Show  Error Alert
@Composable
fun ShowAlert(homeViewModel: HomeViewModel) {
    SimpleAlertDialog(
        title = stringResource(R.string.error_title_text),
        message = homeViewModel.errorMessage.value ?: stringResource(R.string.default_error),
        onDismiss = {
            homeViewModel.dismissErrorDialog()
        }
    )
}

// To show User List
@Composable
fun UserList(users: List<User>, navController: NavHostController) {
    LazyColumn {
        items(count = users.size) { index ->
            val user = users[index]
            UserItem(user) {
                val userJson = user.toJson()
                navController.navigate(Route.DashboardScreen.route.plus("/$userJson"))
            }
        }
    }
}