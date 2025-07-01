package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.CreateReviewDto
import com.example.duskskyapp.data.remote.dto.ReviewDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi {

    // 🔹 Obtener reviews por ID de juego
    @GET("reviews/game/{gameId}")
    suspend fun getReviewsByGame(
        @Path("gameId") gameId: String
    ): List<ReviewDto>

    // 🔹 Crear nueva review
    @POST("reviews/")
    suspend fun postReview(
        @Body review: CreateReviewDto
    ): ReviewDto

    // 🔹 Opcional: obtener reviews recientes por juego (como en JS)
    @GET("reviews/game/{gameId}/recent")
    suspend fun getRecentReviewsByGame(
        @Path("gameId") gameId: String,
        @Query("limit") limit: Int = 6
    ): List<ReviewDto>

    // 🔹 Opcional: obtener reviews top por juego
    @GET("reviews/game/{gameId}/top")
    suspend fun getTopReviewsByGame(
        @Path("gameId") gameId: String,
        @Query("limit") limit: Int = 6
    ): List<ReviewDto>

    // 🔹 Opcional: obtener top reviews generales
    @GET("reviews/top")
    suspend fun getTopReviews(
        @Query("limit") limit: Int = 6
    ): List<ReviewDto>

    // 🔹 Opcional: obtener reviews de amigos
    @GET("reviews/friends")
    suspend fun getReviewsFromFriends(
        @Query("friend_ids") friendIds: List<String>,
        @Query("limit") limit: Int = 6
    ): List<ReviewDto>
}
