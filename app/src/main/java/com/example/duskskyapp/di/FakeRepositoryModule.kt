// src/debug/java/com/example/duskskyapp/di/FakeRepositoryModule.kt
package com.example.duskskyapp.di

import com.example.duskskyapp.data.repository.GameRepository
import com.example.duskskyapp.data.repository.fake.FakeGameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FakeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        fake: FakeGameRepository
    ): GameRepository
}
