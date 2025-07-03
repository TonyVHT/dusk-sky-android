package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.UserDto
import com.example.duskskyapp.data.remote.dto.UserProfileCreateDto
import com.example.duskskyapp.data.remote.dto.UserProfileDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @Multipart
    @PATCH("profiles/{userId}/upload")
    suspend fun uploadProfileContent(
        @Path("userId") userId: String,
        @Part avatar: MultipartBody.Part? = null,
        @Part banner: MultipartBody.Part? = null,
        @Part media: MultipartBody.Part? = null,
        @Part("bio") bio: String? = null,
        @Part("about_section") aboutSection: String? = null,
    ): UserProfileDto

}
