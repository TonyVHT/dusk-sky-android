package com.example.duskskyapp.data.repository

interface UserRepository {
    suspend fun getUserNameById(userId: String): String
}
