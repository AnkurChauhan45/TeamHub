package com.example.teamhub2.domain.repository

import com.example.teamhub2.data.local.entity.EmployeeEntity
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {

    fun getEmployees(): Flow<List<EmployeeEntity>>

    suspend fun refreshEmployees()

    fun getEmployeeById(id: String): Flow<EmployeeEntity?>
}