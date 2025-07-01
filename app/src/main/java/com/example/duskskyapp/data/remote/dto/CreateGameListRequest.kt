package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateGameListRequest(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("isPublic")
    val isPublic: Boolean
)
