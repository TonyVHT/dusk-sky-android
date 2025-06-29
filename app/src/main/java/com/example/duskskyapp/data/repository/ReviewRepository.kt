// Interfaz
package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.model.ReviewUI

interface ReviewRepository {
    suspend fun getReviewsByGame(gameId: String): List<ReviewUI>
    suspend fun postReview(userId: String, gameId: String, content: String, rating: Float): ReviewUI
}
