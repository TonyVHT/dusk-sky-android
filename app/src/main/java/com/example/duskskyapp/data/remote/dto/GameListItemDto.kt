package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GameListItemDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("list_id")
    val listId: String,

    @SerializedName("game_id")
    val gameId: String,

    @SerializedName("comment")
    val comment: String
)
