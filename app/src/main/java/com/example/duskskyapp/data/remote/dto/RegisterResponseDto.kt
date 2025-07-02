package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponseDto(
    @SerializedName("_id")
    val userId: String,
    val username: String,
    val email: String,
    val role: String? = null,
    val status: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)
