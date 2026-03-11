package com.example.teamhub2.data.repository

import com.example.teamhub2.data.local.dao.EmployeeDao
import com.example.teamhub2.data.local.entity.EmployeeEntity
import com.example.teamhub2.data.mapper.toEntity
import com.example.teamhub2.data.remote.api.EmployeeApi
import com.example.teamhub2.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val api: EmployeeApi,
    private val dao: EmployeeDao
) : EmployeeRepository {

    override fun getEmployees(): Flow<List<EmployeeEntity>> {
        return dao.getEmployees()
    }

    override suspend fun refreshEmployees() {
        try {
            val response = api.getEmployees()

            if (response.isSuccessful) {

                val body = response.body() ?: return
                val employees = body.data?.employees ?: return

                val entityList = employees.map { it.toEntity() }

                dao.replaceEmployees(entityList)

            }

        } catch (_: Exception) {
            // ignore network errors, cached data will still show
        }
    }

    override fun getEmployeeById(id: String): Flow<EmployeeEntity?> {
        return dao.getEmployeeById(id)
    }
}