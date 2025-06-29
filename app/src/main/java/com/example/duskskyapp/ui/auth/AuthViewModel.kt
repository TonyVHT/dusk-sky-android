package com.example.duskskyapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.remote.dto.LoginRequestDto
import com.example.duskskyapp.data.remote.dto.RegisterRequestDto
import com.example.duskskyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import android.util.Base64

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val prefs: UserPreferences // ← inyectamos las preferencias
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

    fun login() = viewModelScope.launch {
        val current = _uiState.value
        _uiState.value = current.copy(isLoading = true, errorMessage = null)

        val result = repository.login(
            LoginRequestDto(
                username = current.username,
                password = current.password
            )
        )

        result.fold(
            onSuccess = { resp ->
                val token = resp.accessToken
                val userId = decodeJwtAndGetUserId(token)

                // ✅ Guardar en preferencias
                prefs.saveAuthInfo(token, userId)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    token = token,
                    isLoggedIn = true
                )
            },
            onFailure = { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.message
                )
            }
        )
    }


    fun register() = viewModelScope.launch {
        val current = _uiState.value
        _uiState.value = current.copy(isLoading = true, errorMessage = null)

        val result = repository.register(
            RegisterRequestDto(
                username = current.username,
                email = current.email,
                password = current.password
            )
        )

        result.fold(
            onSuccess = { resp ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    token = resp.userId.toString(),
                    isLoggedIn = true
                )
            },
            onFailure = { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.message
                )
            }
        )
    }

    private fun decodeJwtAndGetUserId(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            val payloadJson = String(Base64.decode(parts[1], Base64.DEFAULT))
            val payload = JSONObject(payloadJson)
            payload.optString("sub", null) ?: payload.optString("_id", null)
        } catch (e: Exception) {
            null
        }
    }
}
