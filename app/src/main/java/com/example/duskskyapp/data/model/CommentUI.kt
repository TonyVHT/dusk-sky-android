package com.example.duskskyapp.data.model

/**
 * Información mínima para mostrar un comentario en UI
 */
data class CommentUI(
    val id: String,
    val author: String,
    val text: String,
    val timestamp: String
)