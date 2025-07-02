package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FriendshipResponseDto(
    @SerializedName("_id")
    val idMongo: String,

    @SerializedName("id")
    val id: String?, // si existe en el backend

    @SerializedName("sender_id")
    val senderId: String,

    @SerializedName("receiver_id")
    val receiverId: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("requested_at")
    val requestedAt: String
)
