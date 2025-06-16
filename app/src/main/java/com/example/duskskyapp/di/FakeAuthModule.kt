//package com.example.duskskyapp.di
//
//import com.example.duskskyapp.data.repository.AuthRepository
//import com.example.duskskyapp.data.repository.fake.FakeAuthRepository
//import dagger.Binds
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
///**
// * Mientras el backend de AuthApi no esté listo,
// * este módulo hace que Hilt proporcione FakeAuthRepository
// * cada vez que alguien dependa de AuthRepository.
// */
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class FakeAuthModule {
//
//    @Binds
//    @Singleton
//    abstract fun bindAuthRepository(
//        fake: FakeAuthRepository
//    ): AuthRepository
//}
