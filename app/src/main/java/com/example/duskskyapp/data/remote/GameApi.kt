package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.model.ImportGameResponse
import com.example.duskskyapp.data.remote.dto.GameDetailsDto
import com.example.duskskyapp.data.remote.dto.GameDto
import com.example.duskskyapp.data.remote.dto.GamePreviewDto
import com.example.duskskyapp.data.remote.dto.ImportResponseDto
import okhttp3.Response
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

    // GameApi.kt
    @GET("/api/game/search")
    suspend fun searchGameByName(@Query("name") name: String): List<GameDto>


    @POST("api/game/import/{steamAppId}")
    suspend fun postImportGame(@Path("steamAppId") steamAppId: Int): ImportGameResponse

    // 🔹 Obtener preview por ID
    @GET("api/game/preview/{id}")
    suspend fun getGamePreviewById(
        @Path("id") gameId: String
    ): GamePreviewDto

    @GET("/api/game/{id}")
    suspend fun getGameById(@Path("id") id: String): GameDto
}
