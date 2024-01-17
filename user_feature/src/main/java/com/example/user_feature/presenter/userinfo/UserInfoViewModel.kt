package com.example.user_feature.presenter.userinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.Response
import com.example.core.base.BaseViewModel
import com.example.core.exception.handleExceptions
import com.example.domain.model.UserInfo
import com.example.domain.usecase.userinfo.GetUserInfoUseCase
import com.example.user_feature.presenter.userinfo.effect.UserInfoEffect
import com.example.user_feature.presenter.userinfo.intent.UserInfoIntent
import com.example.user_feature.presenter.userinfo.state.UserInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UserInfoViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<UserInfoIntent, UserInfoEffect>() {

    private val _userInfoState = MutableStateFlow(UserInfoState())
    val userInfoState: StateFlow<UserInfoState> = _userInfoState

    init {
        processIntent(UserInfoIntent.FetchUserInfo)
    }

    override fun processIntent(intent: UserInfoIntent) {
        when (intent) {
            is UserInfoIntent.FetchUserInfo -> fetchUserInfo(savedStateHandle["userid"])
            is UserInfoIntent.UIIntent.DismissErrorDialog -> dismissErrorDialog()
            is UserInfoIntent.BackPressed.BackPressClicked -> navigateBack()
        }
    }

    private fun fetchUserInfo(userId: String?) {
        viewModelScope.launch {
            updateState(isLoading = true)
            getUserInfoUseCase(userId).collect { response ->
                updateState(isLoading = false)
                handleUserInfoResult(response)
            }
        }
    }

    private fun updateState(
        isLoading: Boolean = _userInfoState.value.isLoading,
        userInfo: UserInfo? = _userInfoState.value.userInfo,
        errorMessage: Int? = _userInfoState.value.errorMessage
    ) {
        _userInfoState.value = UserInfoState(userInfo, isLoading, errorMessage)
    }

    private fun handleUserInfoResult(response: Response<UserInfo>) {
        when (response) {
            is Response.Success -> updateState(userInfo = response.data, errorMessage = null)
            is Response.Error -> updateState(errorMessage = response.exception?.handleExceptions())
        }
    }

    private fun dismissErrorDialog() {
        viewModelScope.launch {
            updateState(errorMessage = null)
        }
    }

    private fun navigateBack() {
        setEffect {
            UserInfoEffect.BackPressEffect.BackPressed
        }
    }
}
