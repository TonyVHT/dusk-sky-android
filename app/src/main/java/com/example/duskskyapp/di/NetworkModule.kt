package com.example.duskskyapp.di

import com.example.duskskyapp.data.remote.AuthApi
import com.example.duskskyapp.data.remote.GameApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    @Named("AUTH_BASE_URL")
    fun provideAuthBaseUrl(): String = "http://192.168.1.113:8001/"

    @Provides @Singleton
    @Named("GAME_BASE_URL")
    fun provideGameBaseUrl(): String = "http://192.168.1.113:8004/"

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides @Singleton
    @Named("AUTH_RETROFIT")
    fun provideAuthRetrofit(
        client: OkHttpClient,
        @Named("AUTH_BASE_URL") baseUrl: String
    ): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    @Named("GAME_RETROFIT")
    fun provideGameRetrofit(
        client: OkHttpClient,
        @Named("GAME_BASE_URL") baseUrl: String
    ): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideAuthApi(@Named("AUTH_RETROFIT") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun provideGameApi(@Named("GAME_RETROFIT") retrofit: Retrofit): GameApi =
        retrofit.create(GameApi::class.java)
}
