package com.example.teamhub2.di

import android.content.Context
import com.example.teamhub2.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilityModule {

    @Provides
    @Singleton
    fun provideNetworkUtils(
        @ApplicationContext context: Context
    ): NetworkUtils {
        return NetworkUtils(context)
    }
}