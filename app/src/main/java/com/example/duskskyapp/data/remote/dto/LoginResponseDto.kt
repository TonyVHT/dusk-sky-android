// app/src/main/java/com/example/duskskyapp/data/remote/dto/LoginResponseDto.kt
package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String
)
