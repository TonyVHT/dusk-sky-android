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
import com.example.duskskyapp.utils.JwtUtils

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
        val usernameTrimmed = current.username.trim() // 👈 Quita espacios extra
        _uiState.value = current.copy(isLoading = true, errorMessage = null)

        val result = repository.login(
            LoginRequestDto(
                username = usernameTrimmed, // Usa el username limpio
                password = current.password
            )
        )

        result.fold(
            onSuccess = { resp ->
                val token = resp.accessToken
                val userId = decodeJwtAndGetUserId(token)
                prefs.saveAuthInfo(token, userId)

                val role = extractRoleFromJwt(token)
                prefs.saveUserRole(role)

                prefs.saveUsername(usernameTrimmed) // También guarda limpio

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    token = token,
                    isLoggedIn = true
                )
            },
            onFailure = { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = getFriendlyErrorMessage(err)
                )
            }
        )
    }


    fun register() = viewModelScope.launch {
        val current = _uiState.value
        val usernameTrimmed = current.username.trim()
        val emailTrimmed = current.email.trim()
        _uiState.value = current.copy(isLoading = true, errorMessage = null)
        Log.d("AuthViewModel", "🔄 Intentando registrar usuario: username=$usernameTrimmed, email=$emailTrimmed")

        val result = repository.register(
            RegisterRequestDto(
                username = usernameTrimmed,
                email = emailTrimmed,
                password = current.password
            )
        )

        result.fold(
            onSuccess = { resp ->
                val userId = resp.userId.toString()
                Log.d("AuthViewModel", "✅ Usuario registrado. userId recibido: $userId, respuesta completa: $resp")

                if (userId == "0" || userId.isBlank()) {
                    Log.e("AuthViewModel", "❌ userId inválido: $userId. No se puede crear perfil.")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Error: El servidor devolvió un ID de usuario inválido."
                    )
                    return@fold
                }

                try {
                    val profileReq = UserProfileCreateDto(user_id = userId)
                    Log.d("AuthViewModel", "➡️ Enviando creación de perfil: $profileReq")
                    val profileResp = userManagerApi.createUserProfile(userId, profileReq)
                    Log.d("AuthViewModel", "✅ Perfil creado exitosamente: $profileResp")

                    // Guarda el username limpio para futuras sesiones
                    prefs.saveUsername(usernameTrimmed)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        token = userId,
                        isLoggedIn = true
                    )
                } catch (e: Exception) {
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
                            errorMessage = getFriendlyErrorMessage(e)
                        )
                    }
                }
            },
            onFailure = { err ->
                Log.e("AuthViewModel", "❌ Error al registrar usuario: ${err.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = getFriendlyErrorMessage(err)
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

    private fun extractRoleFromJwt(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payloadJson = String(Base64.decode(parts[1], Base64.DEFAULT))
            val payload = JSONObject(payloadJson)
            payload.optString("role", null)
        } catch (e: Exception) {
            null
        }
    }

    private fun getFriendlyErrorMessage(e: Throwable): String {
        val msg = e.message ?: ""
        return when {
            msg.contains("404") || msg.contains("Not Found", ignoreCase = true) ->
                "El recurso no fue encontrado. Verifica los datos ingresados."
            msg.contains("409") || msg.contains("Conflict", ignoreCase = true) ->
                "Este usuario ya existe. Prueba con otro nombre de usuario o correo."
            msg.contains("400") || msg.contains("Bad Request", ignoreCase = true) ->
                "Datos inválidos. Por favor revisa la información e inténtalo de nuevo."
            msg.contains("401") || msg.contains("Unauthorized", ignoreCase = true) ->
                "Credenciales incorrectas. Intenta de nuevo."
            msg.contains("timeout", ignoreCase = true) ->
                "La conexión tardó demasiado. Intenta más tarde."
            msg.contains("Failed to connect") || msg.contains("Unable to resolve host", ignoreCase = true) ->
                "No se pudo conectar al servidor. Revisa tu conexión a internet."
            msg.isBlank() ->
                "Ha ocurrido un error inesperado. Intenta más tarde."
            else -> "Error: $msg"
        }
    }


}
