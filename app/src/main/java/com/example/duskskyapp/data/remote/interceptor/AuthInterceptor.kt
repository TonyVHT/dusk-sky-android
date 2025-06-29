package com.example.duskskyapp.data.remote.interceptor

import android.util.Log
import com.example.duskskyapp.data.local.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken()
        Log.d("AuthInterceptor", "Token actual: ${token ?: "null"}")

        val request = if (!token.isNullOrEmpty()) {
            Log.d("AuthInterceptor", "Agregando token al header")
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            Log.w("AuthInterceptor", "No hay token disponible, enviando sin Authorization")
            chain.request()
        }
        return chain.proceed(request)
    }
}
