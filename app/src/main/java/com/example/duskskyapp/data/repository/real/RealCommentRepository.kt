package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.model.CommentUI
import com.example.duskskyapp.data.remote.CommentApi
import com.example.duskskyapp.data.remote.dto.CommentDto
import com.example.duskskyapp.data.repository.CommentRepository
import com.example.duskskyapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealCommentRepository @Inject constructor(
    private val api: CommentApi,
    private val userRepository: UserRepository
) : CommentRepository {

    override suspend fun fetchComments(reviewId: String): List<CommentUI> =
        withContext(Dispatchers.IO) {
            val dtos = api.getCommentsByReview(reviewId)
            dtos.map { dto -> dtoToUi(dto) } // correcto, ahora que dtoToUi es suspend
        }

    override suspend fun postComment(
        reviewId: String,
        authorId: String,
        text: String
    ): CommentUI = withContext(Dispatchers.IO) {
        // construye el DTO (id y date pueden ser null al postear)
        val dto = CommentDto(
            id       = null,
            reviewId = reviewId,
            authorId = authorId,
            text     = text,
            date     = null,
            status   = "visible"
        )
        val created = api.postComment(dto)
        dtoToUi(created)
    }

    private suspend fun dtoToUi(dto: CommentDto): CommentUI {
        val authorName: String = if (dto.authorId.isNullOrEmpty()) {
            "Usuario desconocido"
        } else {
            try {
                userRepository.getUserNameById(dto.authorId) ?: "Usuario desconocido"
            } catch (e: Exception) {
                "Usuario desconocido"
            }
        }

        return CommentUI(
            id = dto.id ?: "",
            author = authorName,
            text = dto.text,
            timestamp = dto.date.orEmpty()
        )
    }



}