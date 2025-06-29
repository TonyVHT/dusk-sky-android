package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.CommentDto
import retrofit2.http.*

interface CommentApi {

    // Obtiene todos los comentarios para una review
    @GET("comments/review/{reviewId}")
    suspend fun getCommentsByReview(
        @Path("reviewId") reviewId: String
    ): List<CommentDto>

    // Publica un nuevo comentario
    @POST("comments")
    suspend fun postComment(
        @Body dto: CommentDto
    ): CommentDto

    // Opcional: otros endpoints
    @GET("comments/{id}")
    suspend fun getCommentById(@Path("id") id: String): CommentDto

    @DELETE("comments/{id}")
    suspend fun deleteComment(@Path("id") id: String)
}