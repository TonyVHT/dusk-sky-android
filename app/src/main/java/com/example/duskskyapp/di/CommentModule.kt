// src/main/java/com/example/duskskyapp/di/CommentModule.kt
package com.example.duskskyapp.di

import com.example.duskskyapp.data.repository.CommentRepository
import com.example.duskskyapp.data.repository.real.RealCommentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommentModule {
    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        impl: RealCommentRepository
    ): CommentRepository
}
