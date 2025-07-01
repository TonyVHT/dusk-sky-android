// app/src/main/java/com/example/duskskyapp/data/remote/dto/GamePreviewDto.kt
package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GamePreviewDto(
    @SerializedName("id")           // antes tenías "_id"
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("headerUrl")    // si tu JSON realmente usa "headerUrl"
    val headerUrl: String?
)
