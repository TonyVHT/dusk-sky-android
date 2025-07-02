package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.remote.FriendshipApi
import com.example.duskskyapp.data.remote.dto.FriendshipRequestDto
import com.example.duskskyapp.data.remote.dto.FriendshipResponseDto
import com.example.duskskyapp.data.repository.FriendshipRepository
import javax.inject.Inject

class RealFriendshipRepository @Inject constructor(
    private val api: FriendshipApi
) : FriendshipRepository {

    override suspend fun sendRequest(dto: FriendshipRequestDto) {
        api.sendFriendRequest(dto)
    }

    override suspend fun acceptRequest(requestId: String) {
        api.acceptFriendRequest(requestId)
    }

    override suspend fun rejectRequest(requestId: String) {
        api.rejectFriendRequest(requestId)
    }

    override suspend fun getPendingRequests(userId: String): List<FriendshipResponseDto> {
        return api.getPendingRequests(userId)
    }

    override suspend fun getFriends(userId: String): List<FriendshipResponseDto> {
        return api.getFriends(userId)
    }
}
