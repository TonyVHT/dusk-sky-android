package com.example.duskskyapp.di

import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.local.UserPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferences(
        impl: UserPreferencesImpl
    ): UserPreferences
}
