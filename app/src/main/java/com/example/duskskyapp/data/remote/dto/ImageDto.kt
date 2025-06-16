package com.example.duskskyapp.data.remote.dto

data class ImageDto(
    val _id: String,
    val game_id: String,
    val header_url: String,
    val screenshots: List<String>
)
