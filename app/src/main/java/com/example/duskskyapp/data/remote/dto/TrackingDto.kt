package com.example.duskskyapp.data.remote.dto

data class TrackingDto(
    val id: String? = null,                   // ID del tracking (puede ser nulo al crear)
    val userId: String,                       // Usuario que hace el seguimiento
    val gameId: String,                       // Juego al que se le hace seguimiento
    val status: String,                       // Estado del seguimiento: playing, completed, etc.
    val rating: Float? = null,                // Calificación del juego (opcional)
    val liked: Boolean = false,               // Si le dio "me gusta"
    val review: String? = null,               // Texto de reseña (opcional)
    val createdAt: String? = null,            // ISO 8601 (opcional, si viene del backend)
    val updatedAt: String? = null             // ISO 8601 (opcional)
)
