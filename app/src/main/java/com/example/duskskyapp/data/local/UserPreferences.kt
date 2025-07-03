package com.example.duskskyapp.data.local

interface UserPreferences {
    suspend fun getUserId(): String?
    suspend fun getUserRole(): String?
    suspend fun clearSession()
    suspend fun saveAuthInfo(token: String, userId: String?)
    suspend fun saveUserRole(role: String?)
    suspend fun getUsername(): String?           // <--- AGREGA ESTO
    suspend fun saveUsername(username: String?)
}
