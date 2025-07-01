package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GameDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("rating")
    val rating: Double = 0.0,

    @SerializedName("headerUrl")
    val coverImageUrl: String? = null
)
