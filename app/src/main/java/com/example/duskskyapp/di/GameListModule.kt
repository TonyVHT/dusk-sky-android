package com.example.duskskyapp.di

import com.example.duskskyapp.data.repository.GameListRepository
import com.example.duskskyapp.data.repository.real.RealGameListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameListModule {

    @Binds
    @Singleton
    abstract fun bindGameListRepository(
        impl: RealGameListRepository
    ): GameListRepository
}
