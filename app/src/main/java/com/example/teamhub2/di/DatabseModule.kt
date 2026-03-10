package com.example.teamhub2.di

import android.content.Context
import androidx.room.Room
import com.example.teamhub2.data.local.dao.EmployeeDao
import com.example.teamhub2.data.local.database.TeamHubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TeamHubDatabase {
        return Room.databaseBuilder(
            context,
            TeamHubDatabase::class.java,
            "teamhub_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(
        database: TeamHubDatabase
    ): EmployeeDao {
        return database.employeeDao()
    }
}