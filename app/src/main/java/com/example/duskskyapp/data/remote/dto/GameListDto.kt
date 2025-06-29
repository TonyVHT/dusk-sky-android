package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GameListDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("is_public")
    val isPublic: Boolean,

    @SerializedName("created_at")
    val createdAt: String
)
