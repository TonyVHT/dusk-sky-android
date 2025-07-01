package com.example.duskskyapp.data.repository.real

import android.util.Log
import com.example.duskskyapp.data.remote.AuthApi
import com.example.duskskyapp.data.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : UserRepository {
    override suspend fun getUserNameById(userId: String): String {
        return try {
            val user = authApi.getUserById(userId)
            Log.d("UserRepository", "Usuario obtenido: ${user.name}")
            user.name
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al obtener usuario: ${e.message}", e)
            "Usuario desconocido"
        }
    }

}
