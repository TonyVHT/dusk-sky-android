package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.GameDetailsDto
import com.example.duskskyapp.data.remote.dto.GamePreviewDto
import com.example.duskskyapp.data.remote.dto.ImportResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GameApi {

    // 🔹 Para DefaultGameRepository.fetchPopular()
    @GET("api/game/previews")
    suspend fun getGamePreviews(): List<GamePreviewDto>

    // 🔹 Para DefaultGameRepository.fetchGameDetail()
    @GET("api/game/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: String
    ): GameDetailsDto

    // 🔹 Para búsqueda rápida con previews (ligeros)
    @GET("api/game/search/preview")
    suspend fun searchPreviewsByName(
        @Query("name") name: String
    ): List<GamePreviewDto>

    // 🔹 Para búsqueda completa (con detalles)
    @GET("api/game/search")
    suspend fun searchDetailsByName(
        @Query("name") name: String
    ): List<GameDetailsDto>

    // 🔹 Importación desde Steam
    @POST("api/game/import/{steamAppId}")
    suspend fun importGameFromSteam(
        @Path("steamAppId") steamAppId: Int
    ): ImportResponseDto

    // 🔹 Obtener preview por ID
    @GET("api/game/preview/{id}")
    suspend fun getGamePreviewById(
        @Path("id") gameId: String
    ): GamePreviewDto
}
