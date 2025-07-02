package com.example.duskskyapp.di

import com.example.duskskyapp.data.remote.FriendshipApi
import com.example.duskskyapp.data.repository.FriendshipRepository
import com.example.duskskyapp.data.repository.real.RealFriendshipRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FriendshipModule {

    @Binds
    @Singleton
    abstract fun bindFriendshipRepository(
        impl: RealFriendshipRepository
    ): FriendshipRepository
}
