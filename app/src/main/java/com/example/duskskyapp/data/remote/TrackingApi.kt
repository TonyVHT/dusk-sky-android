package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.TrackingDto
import com.example.duskskyapp.data.remote.dto.TrackingLookupRequestDto
import retrofit2.http.*

interface TrackingApi {

    @GET("api/trackings/user/{userId}")
    suspend fun getTrackingsByUser(
        @Path("userId") userId: String
    ): List<TrackingDto>

    @GET("api/trackings/{id}")
    suspend fun getTrackingById(
        @Path("id") id: String
    ): TrackingDto

    @POST("api/trackings")
    suspend fun createTracking(
        @Body trackingDto: TrackingDto
    ): TrackingDto

    @PUT("api/trackings/{id}")
    suspend fun updateTracking(
        @Path("id") id: String,
        @Body trackingDto: TrackingDto
    ): TrackingDto

    @DELETE("api/trackings/{id}")
    suspend fun deleteTracking(
        @Path("id") id: String
    )

    @GET("api/trackings/user/{userId}/status/{status}")
    suspend fun getGameIdsByStatus(
        @Path("userId") userId: String,
        @Path("status") status: String
    ): List<String>

    @GET("api/trackings/user/{userId}/liked")
    suspend fun getLikedGameIds(
        @Path("userId") userId: String
    ): List<String>

    @POST("api/trackings/lookup")
    suspend fun getTrackingByUserAndGame(
        @Body request: TrackingLookupRequestDto
    ): TrackingDto?
}
