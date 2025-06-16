package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.LoginRequestDto
import com.example.duskskyapp.data.remote.dto.LoginResponseDto
import com.example.duskskyapp.data.remote.dto.RegisterRequestDto
import com.example.duskskyapp.data.remote.dto.RegisterResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register/")
    suspend fun register(@Body req: RegisterRequestDto): RegisterResponseDto

    @POST("auth/login/")
    suspend fun login(@Body req: LoginRequestDto): LoginResponseDto
}
