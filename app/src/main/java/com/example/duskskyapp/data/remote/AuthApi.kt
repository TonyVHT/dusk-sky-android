package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.LoginRequestDto
import com.example.duskskyapp.data.remote.dto.LoginResponseDto
import com.example.duskskyapp.data.remote.dto.RegisterRequestDto
import com.example.duskskyapp.data.remote.dto.RegisterResponseDto
import com.example.duskskyapp.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @POST("auth/register/")
    suspend fun register(@Body req: RegisterRequestDto): RegisterResponseDto

    @POST("auth/login/")
    suspend fun login(@Body req: LoginRequestDto): LoginResponseDto

    @GET("auth/users/{id}")
    suspend fun getUserById(@Path("id") userId: String): UserDto

    @GET("auth/users/search")
    suspend fun searchUserByUsername(@Query("username") username: String): List<UserDto>

}
