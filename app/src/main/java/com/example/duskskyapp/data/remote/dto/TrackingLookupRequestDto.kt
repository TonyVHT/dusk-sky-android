package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrackingLookupRequestDto(
    @SerializedName("UserId")
    val userId: String,

    @SerializedName("GameId")
    val gameId: String
)
