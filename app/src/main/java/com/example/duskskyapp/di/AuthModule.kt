package com.example.duskskyapp.di

import com.example.duskskyapp.data.repository.AuthRepository
import com.example.duskskyapp.data.repository.real.RealAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: RealAuthRepository
    ): AuthRepository
}
