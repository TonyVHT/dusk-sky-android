package com.example.duskskyapp.data.model

import com.example.duskskyapp.data.remote.dto.ReviewDto

data class ReviewUI(
    val id: String,
    val authorName: String?,
    val content: String,
    val rating: Number,
    val createdAt: String,
    val likes: Int
)

// Función de extensión que convierte ReviewDto a ReviewUI
fun ReviewDto.toUi(): ReviewUI {
    return ReviewUI(
        id = id,
        authorName = userId ?: "Anónimo",
        content = content?:"",
        rating = rating ?: 0,
        createdAt = createdAt ?: "Fecha desconocida",
        likes = likes ?: 0
    )
}
