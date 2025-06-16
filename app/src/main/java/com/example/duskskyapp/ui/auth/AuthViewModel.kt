// app/src/main/java/com/example/duskskyapp/ui/auth/AuthViewModel.kt
package com.example.duskskyapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.remote.dto.LoginRequestDto
import com.example.duskskyapp.data.remote.dto.RegisterRequestDto
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

    fun onUsernameChanged(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername)
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    /** Llama al endpoint POST /login/ */
    fun login() = viewModelScope.launch {
        val current = _uiState.value
        // indicamos carga
        _uiState.value = current.copy(isLoading = true, errorMessage = null)

        // ahora usamos username en lugar de email
        val result = repository.login(
            LoginRequestDto(
                username = current.username,
                password = current.password
            )
        )

        result.fold(
            onSuccess = { resp ->
                // aquí resp.accessToken viene del JSON {"access_token": "...", "token_type":"bearer"}
                _uiState.value = _uiState.value.copy(
                    isLoading  = false,
                    token      = resp.accessToken,
                    isLoggedIn = true
                )
            },
            onFailure = { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading    = false,
                    errorMessage = err.message
                )
            }
        )
    }

    /** Llama al endpoint POST /register/ */
    fun register() = viewModelScope.launch {
        val current = _uiState.value
        _uiState.value = current.copy(isLoading = true, errorMessage = null)

        val result = repository.register(
            RegisterRequestDto(
                username = current.username,
                email    = current.email,
                password = current.password
            )
        )

        result.fold(
            onSuccess = { resp ->
                // aquí no recibes token, así que usamos el userId como placeholder
                _uiState.value = _uiState.value.copy(
                    isLoading   = false,
                    token       = resp.userId.toString(),
                    isLoggedIn  = true
                )
            },
            onFailure = { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading    = false,
                    errorMessage = err.message
                )
            }
        )
    }
}
