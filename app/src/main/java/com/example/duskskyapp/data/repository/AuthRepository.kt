package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.remote.dto.LoginRequest
import com.example.duskskyapp.data.remote.dto.LoginResponse
import com.example.duskskyapp.data.remote.dto.RegisterRequest
import com.example.duskskyapp.data.remote.dto.RegisterResponse

/**
 * Contrato de autenticación.
 * Define los métodos que el repositorio debe exponer.
 */
interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
}
