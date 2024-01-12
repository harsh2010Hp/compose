package com.example.user_feature.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Response
import com.example.domain.model.User
import com.example.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Calls list of user api
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUsersUseCase: GetUserUseCase
) : ViewModel() {

    // For loader
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // For api response
    private val _users: MutableStateFlow<Response<List<User>>> = MutableStateFlow(
        Response.Success(
            listOf()
        )
    )
    val users: StateFlow<Response<List<User>>> = _users

    // For error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // For Error Alert
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    // Loads list of users
    fun getUsers() {
        viewModelScope.launch {
            _loading.emit(true)
            getUsersUseCase().collect { result ->
                _users.emit(result)
                _loading.emit(false)
                if (result is Response.Error) {
                    showErrorDialog(result.message)
                }
            }
        }
    }

    // Show error dialog
    private fun showErrorDialog(message: String?) {
        _errorMessage.value = message
        _showDialog.value = true
    }

    // hides error dialog
    fun dismissErrorDialog() {
        _errorMessage.value = null
        _showDialog.value = false
    }
}