// app/src/main/java/com/example/duskskyapp/data/repository/real/DefaultGameRepository.kt
package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.model.GameDetailUI
import com.example.duskskyapp.data.remote.GameApi
import com.example.duskskyapp.data.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultGameRepository @Inject constructor(
    private val api: GameApi
) : GameRepository {
    override suspend fun fetchPopular(): List<GameUI> = withContext(Dispatchers.IO) {
        api.getGamePreviews()
            // 1) Eliminamos aquellos sin headerUrl
            .mapNotNull { dto ->
                dto.headerUrl
                    ?.takeIf { it.isNotBlank() }
                    ?.let { url ->
                        GameUI(
                            id       = dto.id,
                            title    = dto.name,
                            coverUrl = url
                        )
                    }
            }
            // 2) Limitamos, p. ej., a los primeros 10
            .take(10)
    }
    override suspend fun fetchGameDetail(gameId: String) = api.getGameDetails(gameId).let { dt ->
        GameDetailUI(
            id             = dt.id,
            title          = dt.name.orEmpty(),
            description    = dt.description.orEmpty(),
            developer      = dt.developer.orEmpty(),
            publisher      = dt.publisher.orEmpty(),
            releaseDate    = dt.releaseDate.orEmpty(),
            headerUrl = dt.headerImageUrl.orEmpty(),
            screenshots    = dt.screenshots?.mapNotNull { it } ?: emptyList()
        )
    }
}
