package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.remote.dto.FriendshipRequestDto
import com.example.duskskyapp.data.remote.dto.FriendshipResponseDto

interface FriendshipRepository {
    suspend fun sendRequest(dto: FriendshipRequestDto)
    suspend fun acceptRequest(requestId: String)
    suspend fun rejectRequest(requestId: String)
    suspend fun getPendingRequests(userId: String): List<FriendshipResponseDto>
    suspend fun getFriends(userId: String): List<FriendshipResponseDto>
}
