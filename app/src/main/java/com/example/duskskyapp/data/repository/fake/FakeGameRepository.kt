// app/src/debug/java/com/example/duskskyapp/data/repository/fake/FakeGameRepository.kt
package com.example.duskskyapp.data.repository.fake

import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.model.GameDetailUI
import com.example.duskskyapp.data.repository.GameRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Devuelve ejemplos de GameUI y GameDetailUI para poder probar sin backend.
 */
class FakeGameRepository @Inject constructor() : GameRepository {
    override suspend fun fetchPopular(): List<GameUI> {
        delay(500) // simula latencia
        return listOf(
            GameUI("1", "Dusk Sky",   "https://via.placeholder.com/120x180?text=Dusk+Sky"),
            GameUI("2", "Otro Juego", "https://via.placeholder.com/120x180?text=Otro+Juego"),
            GameUI("3", "Indie Fun",  "https://via.placeholder.com/120x180?text=Indie+Fun")
        )
    }

    override suspend fun fetchGameDetail(gameId: String): GameDetailUI {
        delay(500) // simula latencia
        // Aquí puedes retornar distintos datos según gameId, pero para prueba devolvemos siempre lo mismo
        return GameDetailUI(
            id             = gameId,
            title          = "Título de juego $gameId",
            description    = "Descripción simulada para el juego con id=$gameId.",
            developer      = "Dev Studio",
            publisher      = "Publisher Inc.",
            releaseDate    = "1 Ene, 2025",
            headerImageUrl = "https://via.placeholder.com/600x200?text=Header+$gameId",
            screenshots    = listOf(
                "https://via.placeholder.com/160x90?text=Shot1",
                "https://via.placeholder.com/160x90?text=Shot2",
                "https://via.placeholder.com/160x90?text=Shot3"
            )
        )
    }
}
