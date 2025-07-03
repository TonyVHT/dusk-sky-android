package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.model.GameDetailUI
import com.example.duskskyapp.data.model.ImportGameResponse
import com.example.duskskyapp.data.remote.GameApi
import com.example.duskskyapp.data.remote.dto.GameDto
import com.example.duskskyapp.data.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultGameRepository @Inject constructor(
    private val api: GameApi
) : GameRepository {

    override suspend fun fetchPopular(): List<GameUI> = withContext(Dispatchers.IO) {
        api.getGamePreviews()
            .mapNotNull { dto ->
                dto.headerUrl
                    ?.takeIf { it.isNotBlank() }
                    ?.let { url ->
                        GameUI(
                            id = dto.id,
                            title = dto.name,
                            coverUrl = url
                        )
                    }
            }
            .take(10)
    }

    override suspend fun fetchGameDetail(gameId: String): GameDetailUI =
        api.getGameDetails(gameId).let { dt ->
            GameDetailUI(
                id = dt.id,
                title = dt.name.orEmpty(),
                description = dt.description.orEmpty(),
                developer = dt.developer.orEmpty(),
                publisher = dt.publisher.orEmpty(),
                releaseDate = dt.releaseDate.orEmpty(),
                headerUrl = dt.headerImageUrl.orEmpty(),
                screenshots = dt.screenshots?.mapNotNull { it } ?: emptyList()
            )
        }

    // 🔹 Búsqueda por nombre (para listas)
    override suspend fun searchGames(name: String): List<GameDto> = withContext(Dispatchers.IO) {
        api.searchGameByName(name)
    }
    override suspend fun getGameById(id: String): GameDto = withContext(Dispatchers.IO) {
        println("🌐 Buscando juego por ID: $id")
        val result = api.getGameById(id)
        println("✅ Juego recibido: ${result.title}")
        result
    }
    override suspend fun importGame(steamAppId: Int): ImportGameResponse = withContext(Dispatchers.IO) {
        api.postImportGame(steamAppId)
    }

}

