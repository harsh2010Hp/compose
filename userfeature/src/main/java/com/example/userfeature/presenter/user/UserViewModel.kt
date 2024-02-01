package com.example.userfeature.presenter.user

import androidx.lifecycle.viewModelScope
import com.example.core.Response
import com.example.core.base.BaseViewModel
import com.example.core.exception.handleExceptions
import com.example.domain.model.User
import com.example.domain.usecase.user.GetUserUseCase
import com.example.userfeature.presenter.user.effect.UserEffect
import com.example.userfeature.presenter.user.intent.UserIntent
import com.example.userfeature.presenter.user.state.UserUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUserUseCase
) : BaseViewModel<UserIntent, UserEffect>() {

    private val _userState = MutableStateFlow<UserUIState>(UserUIState.Loading(false))
    val userState: StateFlow<UserUIState> = _userState

    init {
        processIntent(UserIntent.LoadUsers)
    }

    override fun processIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.LoadUsers -> loadUsers()
            is UserIntent.UIIntent.DialogDismissClicked -> dismissErrorDialog()
            is UserIntent.UIIntent.ListItemClicked -> navigateToUserInfoScreen(intent.userId)
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            getUsersUseCase().collect { result ->
                handleResult(result)
            }
        }
    }

    private fun handleResult(result: Response<List<User>>) {
        _userState.value =
            when (result) {
                is Response.Success -> UserUIState.ShowContent(users = result.data ?: emptyList())
                is Response.Error -> UserUIState.Error(
                    errorMessage = result.exception?.handleExceptions(),
                    true
                )

                is Response.Loading -> UserUIState.Loading(result.showLoading)
            }
    }

    private fun dismissErrorDialog() {
        viewModelScope.launch {
            _userState.value = UserUIState.Error(showMessage = false)
        }
    }

    private fun navigateToUserInfoScreen(userId: String?) {
        setEffect {
            UserEffect.NavigationEffect.NavigateUserInfoScreen(userId)
        }
    }
}
