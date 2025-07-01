package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateGameListItemRequest(
    @SerializedName("listId")
    val listId: String,

    @SerializedName("gameId")
    val gameId: String,

    @SerializedName("comment")
    val comment: String? = null
)
