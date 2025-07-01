package com.example.duskskyapp.data.local

interface UserPreferences {
    suspend fun getUserId(): String?
    suspend fun getUserRole(): String?
    suspend fun clearSession()
    suspend fun saveAuthInfo(token: String, userId: String?)
}
