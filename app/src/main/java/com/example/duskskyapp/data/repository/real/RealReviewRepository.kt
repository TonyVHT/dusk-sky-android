package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.model.ReviewUI
import com.example.duskskyapp.data.model.toUi
import com.example.duskskyapp.data.remote.ReviewApi
import com.example.duskskyapp.data.remote.dto.CreateReviewDto
import com.example.duskskyapp.data.repository.ReviewRepository
import javax.inject.Inject

class RealReviewRepository @Inject constructor(
    private val api: ReviewApi
) : ReviewRepository {

    override suspend fun getReviewsByGame(gameId: String): List<ReviewUI> {
        return api.getReviewsByGame(gameId).map { it.toUi() }
    }

    override suspend fun postReview(userId: String, gameId: String, content: String, rating: Float): ReviewUI {
        val dto = CreateReviewDto(
            userId = userId,
            gameId = gameId,
            content = content,
            rating = rating
        )
        return api.postReview(dto).toUi()
    }
}
