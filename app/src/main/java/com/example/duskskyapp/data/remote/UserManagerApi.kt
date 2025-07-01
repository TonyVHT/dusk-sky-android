package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.UserDto
import com.example.duskskyapp.data.remote.dto.UserProfileDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserManagerApi {
    @GET("/profiles/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: String): UserProfileDto

    @GET("/auth/users/search")
    suspend fun searchUserByUsername(@Query("username") username: String): List<UserDto>

}
