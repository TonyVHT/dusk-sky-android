// app/src/main/java/com/example/duskskyapp/data/remote/dto/GameDetailsDto.kt
package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GameDetailsDto(
    @SerializedName("_id")
    val id: String,

    // Asegúrate de que tu API devuelva el nombre aquí:
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("developer")
    val developer: String,

    @SerializedName("publisher")
    val publisher: String,

    @SerializedName("release_date")
    val releaseDate: String,

    // Si en la respuesta JSON las capturas vienen aquí en vez de en ImageDto:
    @SerializedName("screenshots")
    val screenshots: List<String>,

    // El resto de campos originales, si los necesitas:
    @SerializedName("platforms")
    val platforms: Map<String, Boolean> = emptyMap(),

    @SerializedName("genres")
    val genres: List<String> = emptyList()
)
