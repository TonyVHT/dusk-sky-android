package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.FriendshipRequestDto
import com.example.duskskyapp.data.remote.dto.FriendshipResponseDto
import retrofit2.http.*

interface FriendshipApi {

    @POST("/friendships/")
    suspend fun sendFriendRequest(@Body dto: FriendshipRequestDto)

    @PUT("/friendships/{requestId}/accept")
    suspend fun acceptFriendRequest(@Path("requestId") requestId: String)

    @PUT("/friendships/{requestId}/reject")
    suspend fun rejectFriendRequest(@Path("requestId") requestId: String)

    @GET("/friendships/pending/{userId}")
    suspend fun getPendingRequests(@Path("userId") userId: String): List<FriendshipResponseDto>

    @GET("/friendships/user/{userId}")
    suspend fun getFriends(@Path("userId") userId: String): List<FriendshipResponseDto>
}
