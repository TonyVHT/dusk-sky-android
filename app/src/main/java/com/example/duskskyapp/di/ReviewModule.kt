package com.example.duskskyapp.di

import com.example.duskskyapp.data.repository.ReviewRepository
import com.example.duskskyapp.data.repository.real.RealReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewModule {
    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        impl: RealReviewRepository
    ): ReviewRepository
}
