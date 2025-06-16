package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.model.GameDetailUI
import com.example.duskskyapp.data.remote.GameApi
import com.example.duskskyapp.data.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** Implementación real que llama tu API */
class DefaultGameRepository @Inject constructor(
    private val api: GameApi
) : GameRepository {

    override suspend fun fetchPopular(): List<GameUI> = withContext(Dispatchers.IO) {
        val games  = api.getGames()
        val images = api.getImages().associateBy { it.game_id }
        games.mapNotNull { g ->
            images[g._id]?.header_url
                ?.let { url -> GameUI(g._id, g.name, url) }
        }
    }

    override suspend fun fetchGameDetail(gameId: String): GameDetailUI = withContext(Dispatchers.IO) {
        // 1) Detalle
        val dt = api.getGameDetails(gameId)
        // 2) Imágenes
        val imgDto = api.getImages().firstOrNull { it.game_id == gameId }
        GameDetailUI(
            id             = dt.id,
            title          = dt.name ?: "",
            description    = dt.description,
            developer      = dt.developer,
            publisher      = dt.publisher,
            releaseDate    = dt.releaseDate,
            headerImageUrl = imgDto?.header_url.orEmpty(),
            screenshots    = imgDto?.screenshots.orEmpty()
        )
    }
}
