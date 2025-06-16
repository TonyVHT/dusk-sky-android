package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.remote.dto.LoginRequestDto
import com.example.duskskyapp.data.remote.dto.LoginResponseDto
import com.example.duskskyapp.data.remote.dto.RegisterRequestDto
import com.example.duskskyapp.data.remote.dto.RegisterResponseDto

/**
 * Contrato de autenticación.
 * Define los métodos que el repositorio debe exponer.
 */
interface AuthRepository {
    suspend fun login(request: LoginRequestDto): Result<LoginResponseDto>
    suspend fun register(request: RegisterRequestDto): Result<RegisterResponseDto>
}
