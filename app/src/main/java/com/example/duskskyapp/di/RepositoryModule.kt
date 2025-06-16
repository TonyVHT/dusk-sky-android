//// src/main/java/com/example/duskskyapp/di/RepositoryModule.kt
//package com.example.duskskyapp.di
//
//import com.example.duskskyapp.data.repository.GameRepository
//import com.example.duskskyapp.data.repository.real.DefaultGameRepository
//import dagger.Binds
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class RepositoryModule {
//    @Binds
//    @Singleton
//    abstract fun bindGameRepository(
//        impl: DefaultGameRepository
//    ): GameRepository
//}
