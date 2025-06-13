package com.example.duskskyapp.ui.auth

/**
 * Representa el estado de la UI de autenticación:
 * - email, password y (opcionalmente) username
 * - isLoading: para mostrar spinner
 * - errorMessage: mensaje de error si falla
 * - token/isLoggedIn: para disparar navegación al Home
 */
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val username: String = "",    // solo para la pantalla de registro
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val token: String? = null,
    val isLoggedIn: Boolean = false
)
