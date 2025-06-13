package com.example.duskskyapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.remote.dto.LoginRequest
import com.example.duskskyapp.data.remote.dto.RegisterRequest
import com.example.duskskyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onEmailChanged(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onUsernameChanged(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername)
    }

    fun login() {
        val current = _uiState.value
        viewModelScope.launch {
            _uiState.value = current.copy(isLoading = true, errorMessage = null)
            val result = repository.login(
                LoginRequest(email = current.email, password = current.password)
            )
            if (result.isSuccess) {
                val resp = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    token = resp.token,
                    isLoggedIn = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun register() {
        val current = _uiState.value
        viewModelScope.launch {
            _uiState.value = current.copy(isLoading = true, errorMessage = null)
            val result = repository.register(
                RegisterRequest(
                    username = current.username,
                    email = current.email,
                    password = current.password
                )
            )
            if (result.isSuccess) {
                val resp = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    token = resp.userId.toString(),
                    isLoggedIn = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
}
