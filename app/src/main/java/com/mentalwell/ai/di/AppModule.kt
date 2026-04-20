package com.mentalwell.ai.di

import android.content.Context
import com.mentalwell.ai.data.repository.AuthRepositoryImpl
import com.mentalwell.ai.domain.repository_interface.AuthRepository
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

    /**
     * Provides the application context robustly to other dependencies.
     */
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    /**
     * Binds AuthRepositoryImpl to the AuthRepository interface.
     */
    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository {
        return impl
    }
}
