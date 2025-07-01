package com.example.duskskyapp.di

import com.example.duskskyapp.data.repository.UserTrackingRepository
import com.example.duskskyapp.data.repository.real.RealUserTrackingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserTrackingModule {

    @Binds
    @Singleton
    abstract fun bindUserTrackingRepository(
        impl: RealUserTrackingRepository
    ): UserTrackingRepository
}
