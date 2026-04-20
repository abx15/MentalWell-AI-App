package com.mentalwell.ai.di

import android.content.Context
import com.mentalwell.ai.data.repository.AuthRepositoryImpl
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.data.repository.MoodRepositoryImpl
import com.mentalwell.ai.domain.repository_interface.MoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Handles dependency injection for App-level components and bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository {
        return impl
    }

    /**
     * Binds MoodRepositoryImpl to the MoodRepository interface.
     */
    @Provides
    @Singleton
    fun provideMoodRepository(impl: MoodRepositoryImpl): MoodRepository {
        return impl
    }
}
