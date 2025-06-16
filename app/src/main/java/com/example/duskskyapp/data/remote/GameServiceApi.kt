package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.CreatedResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.duskskyapp.data.remote.dto.GamePreviewDto
import com.example.duskskyapp.data.remote.dto.GameDetailsDto

interface GameServiceApi {
    // GET http://<host>:8004/api/Game/previews
    @GET("api/Game/previews")
    suspend fun getGamePreviews(): List<GamePreviewDto>

    // GET http://<host>:8004/api/Game/{id}
    @GET("api/Game/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: String
    ): GameDetailsDto

    // (opcional) POST http://<host>:8004/api/Game/import/{steamAppId}
    @POST("api/Game/import/{steamAppId}")
    suspend fun importFromSteam(
        @Path("steamAppId") steamAppId: Int
    ): CreatedResponseDto
}