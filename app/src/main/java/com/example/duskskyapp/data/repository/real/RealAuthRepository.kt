package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.remote.AuthApi
import com.example.duskskyapp.data.remote.dto.LoginRequestDto
import com.example.duskskyapp.data.remote.dto.RegisterRequestDto
import com.example.duskskyapp.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealAuthRepository @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun register(req: RegisterRequestDto) =
        withContext(Dispatchers.IO) { runCatching { api.register(req) } }

    override suspend fun login(req: LoginRequestDto) =
        withContext(Dispatchers.IO) { runCatching { api.login(req) } }
}
