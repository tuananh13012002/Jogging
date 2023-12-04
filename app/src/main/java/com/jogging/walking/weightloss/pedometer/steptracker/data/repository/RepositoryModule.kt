package com.jogging.walking.weightloss.pedometer.steptracker.data.repository

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun providesCharacterRepository(characterRepositoryImpl: CharacterRepositoryImpl): CharacterRepository

//    @Singleton
//    @Binds
//    abstract fun providesUserRepositoryImpl(userRepositoryImpl: UserRepositoryImpl): UserRepository
}