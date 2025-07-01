// utils/JwtUtils.kt
package com.example.duskskyapp.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    fun decodeJwtAndGetUserId(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payloadJson = String(Base64.decode(parts[1], Base64.DEFAULT))
            val payload = JSONObject(payloadJson)
            payload.getString("sub") // O prueba con "_id" si no viene "sub"
        } catch (e: Exception) {
            null
        }
    }
}
