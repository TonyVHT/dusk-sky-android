package com.example.duskskyapp.data.remote.dto

// package com.example.duskskyapp.data.remote.dto
data class CreateReviewDto(
    val userId: String,
    val gameId: String,
    val content: String,
    val rating: Float
)
