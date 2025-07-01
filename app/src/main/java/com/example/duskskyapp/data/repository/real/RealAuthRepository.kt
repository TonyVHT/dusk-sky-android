package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.remote.AuthApi
import com.example.duskskyapp.data.remote.dto.LoginRequestDto
import com.example.duskskyapp.data.remote.dto.LoginResponseDto
import com.example.duskskyapp.data.remote.dto.RegisterRequestDto
import com.example.duskskyapp.data.remote.dto.RegisterResponseDto
import com.example.duskskyapp.data.repository.AuthRepository
import com.example.duskskyapp.utils.JwtUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealAuthRepository @Inject constructor(
    private val api: AuthApi,
    private val userPrefs: UserPreferences
) : AuthRepository {
    override suspend fun login(req: LoginRequestDto): Result<LoginResponseDto> =
        withContext(Dispatchers.IO) {
            runCatching {
                val resp = api.login(req)
                val userId = JwtUtils.decodeJwtAndGetUserId(resp.accessToken)
                userPrefs.saveAuthInfo(resp.accessToken, userId)
                resp
            }
        }

    override suspend fun register(req: RegisterRequestDto): Result<RegisterResponseDto> =
        withContext(Dispatchers.IO) {
            runCatching { api.register(req) }
        }
}
