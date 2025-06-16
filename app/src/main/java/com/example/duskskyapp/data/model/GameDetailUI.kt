package com.example.duskskyapp.data.model

/**
 * Representa la información completa de un juego para la UI.
 */
data class GameDetailUI(
    val id: String,
    val title: String,
    val description: String,
    val developer: String,
    val publisher: String,
    val releaseDate: String,
    val headerImageUrl: String,
    val screenshots: List<String>
)
