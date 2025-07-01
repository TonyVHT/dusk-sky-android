package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.model.GameDetailUI
import com.example.duskskyapp.data.remote.dto.GameDto

/** Contrato común para repositorios de juegos */
interface GameRepository {
    suspend fun fetchPopular(): List<GameUI>
    suspend fun fetchGameDetail(gameId: String): GameDetailUI
    suspend fun searchGames(name: String): List<GameDto>
    suspend fun getGameById(id: String): GameDto
}
