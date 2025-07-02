package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.UserDto
import com.example.duskskyapp.data.remote.dto.UserProfileCreateDto
import com.example.duskskyapp.data.remote.dto.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserManagerApi {
    @GET("profiles/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: String): UserProfileDto

    @PUT("profiles/{userId}")
    suspend fun createUserProfile(
        @Path("userId") userId: String,
        @Body dto: UserProfileCreateDto
    ): UserProfileDto

}
