package com.example.duskskyapp.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserPreferencesImpl @Inject constructor(
    context: Context
) : UserPreferences, TokenProvider {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override fun getToken(): String? {
        return prefs.getString("token", null)
    }

    override suspend fun getUserId(): String? = withContext(Dispatchers.IO) {
        prefs.getString("userId", null)
    }

    override suspend fun getUserRole(): String? = withContext(Dispatchers.IO) {
        prefs.getString("role", null)
    }

    override suspend fun clearSession() = withContext(Dispatchers.IO) {
        prefs.edit().clear().apply()
    }

    override suspend fun saveAuthInfo(token: String, userId: String?) = withContext(Dispatchers.IO) {
        prefs.edit().putString("token", token).apply()
        if (userId != null) {
            prefs.edit().putString("userId", userId).apply()
        }
    }
}

