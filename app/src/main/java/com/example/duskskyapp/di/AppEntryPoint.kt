package com.example.duskskyapp.di

import com.example.duskskyapp.data.local.UserPreferences
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun userPreferences(): UserPreferences
}
