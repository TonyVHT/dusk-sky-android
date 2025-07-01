// app/src/main/java/com/example/duskskyapp/data/remote/dto/GameDetailsDto.kt
package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// app/src/main/java/com/example/duskskyapp/data/remote/dto/GameDetailsDto.kt
data class GameDetailsDto(
    @SerializedName("id")           val id: String,
    @SerializedName("name")          val name: String?,
    @SerializedName("description")   val description: String?,
    @SerializedName("developer")     val developer: String?,
    @SerializedName("publisher")     val publisher: String?,
    @SerializedName("release_date")  val releaseDate: String?,
    @SerializedName("headerUrl")       val headerImageUrl: String?, // <--- si viene en el JSON
    @SerializedName("screenshots")   val screenshots: List<String?>
)
