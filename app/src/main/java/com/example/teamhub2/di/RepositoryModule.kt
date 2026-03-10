package com.example.teamhub2.di

import com.example.teamhub2.data.local.dao.EmployeeDao
import com.example.teamhub2.data.remote.api.EmployeeApi
import com.example.teamhub2.data.repository.EmployeeRepositoryImpl
import com.example.teamhub2.domain.repository.EmployeeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEmployeeRepository(
        api: EmployeeApi,
        dao: EmployeeDao
    ): EmployeeRepository {
        return EmployeeRepositoryImpl(api, dao)
    }
}