package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.model.GameDetailUI

/** Contrato común para repositorios de juegos */
interface GameRepository {
    suspend fun fetchPopular(): List<GameUI>
    suspend fun fetchGameDetail(gameId: String): GameDetailUI  // ← nuevo método
}
