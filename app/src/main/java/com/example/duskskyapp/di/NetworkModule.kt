package com.example.duskskyapp.di

import com.example.duskskyapp.data.local.TokenProvider
import com.example.duskskyapp.data.remote.*
import com.example.duskskyapp.data.remote.interceptor.AuthInterceptor
import com.example.duskskyapp.data.remote.GameListApi
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

    // === Base URLs ===
    @Provides @Singleton @Named("AUTH_BASE_URL")
    fun provideAuthBaseUrl(): String = "http://192.168.1.68:8001/"

    @Provides @Singleton @Named("GAME_BASE_URL")
    fun provideGameBaseUrl(): String = "http://192.168.1.68:8004/"

    @Provides @Singleton @Named("COMMENT_BASE_URL")
    fun provideCommentBaseUrl(): String = "http://192.168.1.68:8008/"

    @Provides @Singleton @Named("REVIEW_BASE_URL")
    fun provideReviewBaseUrl(): String = "http://192.168.1.68:8007/"

    @Provides @Singleton @Named("TRACKING_BASE_URL")
    fun provideTrackingBaseUrl(): String = "http://192.168.1.68:8005/"

    @Provides @Singleton @Named("USERMANAGER_BASE_URL")
    fun provideUserManagerBaseUrl(): String = "http://192.168.1.68:8003/"

    @Provides
    @Singleton
    @Named("GAMELIST_BASE_URL")
    fun provideGameListBaseUrl(): String = "http://192.168.1.68:8010/"




    // === Interceptor ===
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: TokenProvider): AuthInterceptor {
        return AuthInterceptor(tokenProvider)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()


    // === Retrofit instances ===
    @Provides @Singleton @Named("AUTH_RETROFIT")
    fun provideAuthRetrofit(
        client: OkHttpClient,
        @Named("AUTH_BASE_URL") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides @Singleton @Named("GAME_RETROFIT")
    fun provideGameRetrofit(
        client: OkHttpClient,
        @Named("GAME_BASE_URL") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides @Singleton @Named("COMMENT_RETROFIT")
    fun provideCommentRetrofit(
        client: OkHttpClient,
        @Named("COMMENT_BASE_URL") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides @Singleton @Named("REVIEW_RETROFIT")
    fun provideReviewRetrofit(
        client: OkHttpClient,
        @Named("REVIEW_BASE_URL") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides @Singleton @Named("TRACKING_RETROFIT")
    fun provideTrackingRetrofit(
        client: OkHttpClient,
        @Named("TRACKING_BASE_URL") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides @Singleton @Named("USERMANAGER_RETROFIT")
    fun provideUserManagerRetrofit(
        client: OkHttpClient,
        @Named("USERMANAGER_BASE_URL") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Named("GAMELIST_RETROFIT")
    fun provideGameListRetrofit(
        client: OkHttpClient,
        @Named("GAMELIST_BASE_URL") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()




    // === API interfaces ===
    @Provides @Singleton
    fun provideAuthApi(@Named("AUTH_RETROFIT") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun provideGameApi(@Named("GAME_RETROFIT") retrofit: Retrofit): GameApi =
        retrofit.create(GameApi::class.java)

    @Provides @Singleton
    fun provideCommentApi(@Named("COMMENT_RETROFIT") retrofit: Retrofit): CommentApi =
        retrofit.create(CommentApi::class.java)

    @Provides @Singleton
    fun provideReviewApi(@Named("REVIEW_RETROFIT") retrofit: Retrofit): ReviewApi =
        retrofit.create(ReviewApi::class.java)

    @Provides
    @Singleton
    fun provideTrackingApi(@Named("GAME_RETROFIT") retrofit: Retrofit): TrackingApi {
        return retrofit.create(TrackingApi::class.java)
    }

    @Provides @Singleton
    fun provideUserManagerApi(@Named("USERMANAGER_RETROFIT") retrofit: Retrofit): UserManagerApi =
        retrofit.create(UserManagerApi::class.java)

    @Provides
    @Singleton
    fun provideGameListApi(@Named("GAMELIST_RETROFIT") retrofit: Retrofit): GameListApi =
        retrofit.create(GameListApi::class.java)


}
