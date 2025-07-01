package com.example.duskskyapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.model.CommentUI
import com.example.duskskyapp.data.model.GameDetailUI
import com.example.duskskyapp.data.model.ReviewUI
import com.example.duskskyapp.data.repository.CommentRepository
import com.example.duskskyapp.data.repository.GameRepository
import com.example.duskskyapp.data.repository.ReviewRepository
import com.example.duskskyapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val gameRepo: GameRepository,
    private val reviewRepo: ReviewRepository,
    private val userPrefs: UserPreferences,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _gameDetail = MutableStateFlow<GameDetailUI?>(null)
    val gameDetail: StateFlow<GameDetailUI?> = _gameDetail

    private val _reviews = MutableStateFlow<List<ReviewUI>>(emptyList())
    val reviews: StateFlow<List<ReviewUI>> = _reviews

    private val authorCache = mutableMapOf<String, String>()

    fun loadGame(gameId: String) = viewModelScope.launch {
        _gameDetail.value = gameRepo.fetchGameDetail(gameId)
    }

    fun loadReviews(gameId: String) = viewModelScope.launch {
        val raw = reviewRepo.getReviewsByGame(gameId)

        val enriched = raw.map { review ->
            val authorName = authorCache.getOrPut(review.authorName?: "Autor desconocido") {
                try {
                    userRepo.getUserNameById(review.authorName?: "Autor desconocido")
                } catch (e: Exception) {
                    "Usuario desconocido"
                }
            }
            review.copy(authorName = authorName)
        }

        _reviews.value = enriched
    }

    fun postReview(gameId: String, text: String, rating: Float) = viewModelScope.launch {
        val userId = userPrefs.getUserId()
        if (userId != null) {
            val newReview = reviewRepo.postReview(userId, gameId, text, rating)

            val authorName = authorCache.getOrPut(userId) {
                try {
                    userRepo.getUserNameById(userId)
                } catch (e: Exception) {
                    "Usuario desconocido"
                }
            }

            loadReviews(gameId);
        } else {
            // TODO: mostrar mensaje de error o redirigir al login
        }
    }

}
