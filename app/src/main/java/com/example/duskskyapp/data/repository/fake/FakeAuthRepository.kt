//package com.example.duskskyapp.data.repository.fake
//
//import com.example.duskskyapp.data.remote.dto.LoginRequestDto
//import com.example.duskskyapp.data.remote.dto.LoginResponseDto
//import com.example.duskskyapp.data.remote.dto.RegisterRequestDto
//import com.example.duskskyapp.data.remote.dto.RegisterResponseDto
//import com.example.duskskyapp.data.repository.AuthRepository
//import kotlinx.coroutines.delay
//import javax.inject.Inject    // ← importar esto
//
///**
// * Implementación de prueba de AuthRepository.
// * Simula respuestas de red para login y registro.
// */
//class FakeAuthRepository @Inject constructor() : AuthRepository {
//    override suspend fun login(request: LoginRequestDto): Result<LoginResponseDto> {
//        delay(500)
//        return if (request.email.contains("@") && request.password.length >= 4) {
//            Result.success(LoginResponseDto(token = "fake_token", userId = 1L))
//        } else {
//            Result.failure(Exception("Credenciales inválidas (simulado)"))
//        }
//    }
//
//    override suspend fun register(request: RegisterRequestDto): Result<RegisterResponseDto> {
//        delay(500)
//        return if (request.username.isNotBlank() && request.email.contains("@") && request.password.length >= 4) {
//            Result.success(RegisterResponseDto(userId = 2L, username = request.username, email = request.email))
//        } else {
//            Result.failure(Exception("Datos inválidos (simulado)"))
//        }
//    }
//}
