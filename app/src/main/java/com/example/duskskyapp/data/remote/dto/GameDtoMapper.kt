package com.example.duskskyapp.data.remote.dto

import com.example.duskskyapp.data.model.GameUI

fun GameDto.toGameUI(): GameUI = GameUI(
    id       = id,
    title    = title,
    coverUrl = coverImageUrl ?: ""
)

fun List<GameDto>.toGameUIList(): List<GameUI> = this.map { it.toGameUI() }
