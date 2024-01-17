package com.example.user_feature.presenter.home

import androidx.lifecycle.viewModelScope
import com.example.core.Response
import com.example.core.base.BaseViewModel
import com.example.core.exception.handleExceptions
import com.example.domain.model.User
import com.example.domain.usecase.user.GetUserUseCase
import com.example.user_feature.presenter.home.effect.UserEffect
import com.example.user_feature.presenter.home.intent.UserIntent
import com.example.user_feature.presenter.home.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUserUseCase
) : BaseViewModel<UserIntent, UserEffect>() {

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

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
            updateState(isLoading = true)
            getUsersUseCase().collect { result ->
                updateState(isLoading = false)
                handleResult(result)
            }
        }
    }

    private fun updateState(
        isLoading: Boolean = _userState.value.isLoading,
        users: List<User>? = _userState.value.users,
        errorMessage: Int? = _userState.value.errorMessage
    ) {
        _userState.value = UserState(users, isLoading, errorMessage)
    }

    private fun handleResult(result: Response<List<User>>) {
        when (result) {
            is Response.Success -> updateState(users = result.data ?: listOf())
            is Response.Error -> updateState(errorMessage = result.exception?.handleExceptions())
        }
    }

    private fun dismissErrorDialog() {
        viewModelScope.launch {
            updateState(errorMessage = null)
        }
    }

    private fun navigateToUserInfoScreen(userId: String?) {
        setEffect {
            UserEffect.NavigationEffect.NavigateUserInfoScreen(userId)
        }
    }
}
