package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.model.CommentUI

interface CommentRepository {
    suspend fun fetchComments(reviewId: String): List<CommentUI>
    suspend fun postComment(reviewId: String, authorId: String, text: String): CommentUI
}