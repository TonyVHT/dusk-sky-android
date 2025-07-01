package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.remote.GameApi
import com.example.duskskyapp.data.remote.TrackingApi
import com.example.duskskyapp.data.model.GameTrackingUI
import com.example.duskskyapp.data.repository.UserTrackingRepository
import retrofit2.HttpException
import javax.inject.Inject

class RealUserTrackingRepository @Inject constructor(
    private val trackingApi: TrackingApi,
    private val gameApi: GameApi
) : UserTrackingRepository {

    override suspend fun getUserTrackings(userId: String): List<GameTrackingUI> {
        val trackings = try {
            trackingApi.getTrackingsByUser(userId)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                emptyList()
            } else {
                throw e
            }
        }

        return trackings.mapNotNull { t ->
            val game = try {
                gameApi.getGamePreviewById(t.gameId)
            } catch (e: Exception) {
                null // Si falla uno, lo omitimos
            }

            game?.let {
                val normalizedStatus = when (t.status?.lowercase()) {
                    "played", "playing", "backlog", "abandoned" -> t.status.lowercase()
                    else -> "played" // Valor por defecto si el status es inválido o desconocido
                }

                GameTrackingUI(
                    gameId     = it.id,
                    gameTitle  = it.name,
                    gameImage  = it.headerUrl.orEmpty(),
                    rating     = t.rating ?: 0f,
                    liked      = t.liked,
                    hasReview  = !t.review.isNullOrBlank(),
                    status     = normalizedStatus
                )
            }
        }
    }
}
