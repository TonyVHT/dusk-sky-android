package com.example.duskskyapp.data.remote.dto

data class ReviewDto(
    val id: String,
    val userId: String,
    val gameId: String,
    val content: String,
    val rating: Float,
    val createdAt: String,
    val likes: Int,
    val likedBy: List<String>
)
