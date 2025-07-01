package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Refleja JSON de CommentService:
 * {
 *   id, reviewId, authorId, text, date, status
 * }
 */
data class CommentDto(
    @SerializedName("id")        val id: String?,
    @SerializedName("reviewId")  val reviewId: String,
    @SerializedName("authorId")  val authorId: String?,
    @SerializedName("text")      val text: String,
    @SerializedName("date")      val date: String?,
    @SerializedName("status")    val status: String?
)