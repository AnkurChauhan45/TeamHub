package com.example.teamhub2.di

import android.content.Context
import androidx.room.Room
import com.example.teamhub2.data.local.dao.EmployeeDao
import com.example.teamhub2.data.local.database.TeamHubDatabase
import com.example.teamhub2.data.remote.api.EmployeeApi
import com.example.teamhub2.data.repository.EmployeeRepositoryImpl
import com.example.teamhub2.domain.repository.EmployeeRepository
import com.example.teamhub2.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // ------------------
    // RETROFIT
    // ------------------
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://employee-static-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideEmployeeApi(retrofit: Retrofit): EmployeeApi {
        return retrofit.create(EmployeeApi::class.java)
    }

    // ------------------
    // ROOM
    // ------------------
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TeamHubDatabase {
        return Room.databaseBuilder(
            context,
            TeamHubDatabase::class.java,
            "teamhub_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(
        database: TeamHubDatabase
    ): EmployeeDao {
        return database.employeeDao()
    }

    // ------------------
    // NETWORK UTILS
    // ------------------
    @Provides
    @Singleton
    fun provideNetworkUtils(
        @ApplicationContext context: Context
    ): NetworkUtils {
        return NetworkUtils(context)
    }

    // ------------------
    // REPOSITORY
    // ------------------
    @Provides
    @Singleton
    fun provideEmployeeRepository(
        api: EmployeeApi,
        dao: EmployeeDao
    ): EmployeeRepository {
        return EmployeeRepositoryImpl(api, dao)
    }
}