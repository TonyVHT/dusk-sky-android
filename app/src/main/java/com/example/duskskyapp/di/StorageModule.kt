package com.example.duskskyapp.di

import android.content.Context
import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.local.UserPreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context
    ): UserPreferencesImpl {
        return UserPreferencesImpl(context)
    }
}
