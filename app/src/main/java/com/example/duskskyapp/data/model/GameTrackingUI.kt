package com.example.duskskyapp.data.model

data class GameTrackingUI(
    val gameId: String,
    val gameTitle: String,
    val gameImage: String,
    val rating: Float,
    val status: String,     // <- MUY IMPORTANTE
    val liked: Boolean,
    val hasReview: Boolean
)
