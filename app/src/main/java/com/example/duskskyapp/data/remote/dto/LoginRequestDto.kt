// app/src/main/java/com/example/duskskyapp/data/remote/dto/LoginRequestDto.kt
package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)
