package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GameListItemDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("listId") // <--- CAMBIO
    val listId: String,

    @SerializedName("gameId") // <--- CAMBIO
    val gameId: String,

    @SerializedName("comment")
    val comment: String
)
