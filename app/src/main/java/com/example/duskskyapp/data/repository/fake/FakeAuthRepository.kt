package com.example.duskskyapp.data.repository.fake

import com.example.duskskyapp.data.remote.dto.LoginRequest
import com.example.duskskyapp.data.remote.dto.LoginResponse
import com.example.duskskyapp.data.remote.dto.RegisterRequest
import com.example.duskskyapp.data.remote.dto.RegisterResponse
import com.example.duskskyapp.data.repository.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject    // ← importar esto

/**
 * Implementación de prueba de AuthRepository.
 * Simula respuestas de red para login y registro.
 */
class FakeAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        delay(500)
        return if (request.email.contains("@") && request.password.length >= 4) {
            Result.success(LoginResponse(token = "fake_token", userId = 1L))
        } else {
            Result.failure(Exception("Credenciales inválidas (simulado)"))
        }
    }

    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        delay(500)
        return if (request.username.isNotBlank() && request.email.contains("@") && request.password.length >= 4) {
            Result.success(RegisterResponse(userId = 2L, username = request.username, email = request.email))
        } else {
            Result.failure(Exception("Datos inválidos (simulado)"))
        }
    }
}
