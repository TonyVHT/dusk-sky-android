// app/src/main/java/com/example/duskskyapp/data/remote/GameApi.kt
package com.example.duskskyapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import com.example.duskskyapp.data.remote.dto.GameDto
import com.example.duskskyapp.data.remote.dto.ImageDto
import com.example.duskskyapp.data.remote.dto.GameDetailsDto

interface GameApi {
    @GET("api/Game/previews")
    suspend fun getGames(): List<GameDto>

    @GET("api/Image")
    suspend fun getImages(): List<ImageDto>

    // ← Aquí añades el método que faltaba
    @GET("api/Game/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: String
    ): GameDetailsDto
}
