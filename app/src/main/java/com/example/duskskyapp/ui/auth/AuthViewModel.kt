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
import android.util.Log
import com.example.duskskyapp.data.remote.UserManagerApi
import com.example.duskskyapp.data.remote.dto.UserProfileCreateDto
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val prefs: UserPreferences,
    private val userManagerApi: UserManagerApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onEmailChanged(newEmail: String) { _uiState.value = _uiState.value.copy(email = newEmail) }
    fun onUsernameChanged(newUsername: String) { _uiState.value = _uiState.value.copy(username = newUsername) }
    fun onPasswordChanged(newPassword: String) { _uiState.value = _uiState.value.copy(password = newPassword) }

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
        Log.d("AuthViewModel", "🔄 Intentando registrar usuario: username=${current.username}, email=${current.email}")

        val result = repository.register(
            RegisterRequestDto(
                username = current.username,
                email = current.email,
                password = current.password
            )
        )

        result.fold(
            onSuccess = { resp ->
                val userId = resp.userId.toString()
                Log.d("AuthViewModel", "✅ Usuario registrado. userId recibido: $userId, respuesta completa: $resp")

                // ⚠️ Solo crea el perfil si userId es válido
                if (userId == "0" || userId.isBlank()) {
                    Log.e("AuthViewModel", "❌ userId inválido: $userId. No se puede crear perfil.")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Error: El servidor devolvió un ID de usuario inválido."
                    )
                    return@fold
                }

                // ---- Crea el perfil ----
                try {
                    val profileReq = UserProfileCreateDto(user_id = userId)
                    Log.d("AuthViewModel", "➡️ Enviando creación de perfil: $profileReq")
                    val profileResp = userManagerApi.createUserProfile(userId, profileReq)
                    Log.d("AuthViewModel", "✅ Perfil creado exitosamente: $profileResp")

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        token = userId,
                        isLoggedIn = true
                    )
                } catch (e: Exception) {
                    // Maneja el caso de conflicto (409) como éxito
                    if (e.message?.contains("409") == true) {
                        Log.w("AuthViewModel", "⚠️ Perfil ya existe, continuando con registro.")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            token = userId,
                            isLoggedIn = true
                        )
                    } else {
                        Log.e("AuthViewModel", "❌ Error al crear perfil: ${e.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Error al crear perfil: ${e.message}"
                        )
                    }
                }
            },
            onFailure = { err ->
                Log.e("AuthViewModel", "❌ Error al registrar usuario: ${err.message}")
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
