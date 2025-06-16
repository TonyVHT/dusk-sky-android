package com.example.duskskyapp.data.remote.dto

data class GameDto(
    val _id: String,
    val steam_appid: Int?,  // Steam ID or null
    val name: String
)
