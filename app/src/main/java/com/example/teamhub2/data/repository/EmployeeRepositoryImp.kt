package com.example.teamhub2.data.repository

import android.util.Log
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

            Log.d("API_DEBUG", "Success: ${response.isSuccessful}")
            Log.d("API_DEBUG", "Code: ${response.code()}")

            if (response.isSuccessful) {

                val body = response.body()

                if (body == null) {
                    Log.e("API_DEBUG", "Response body is NULL")
                    return
                }

                val employees = body.data?.employees

                if (employees == null) {
                    Log.e("API_DEBUG", "Employees list is NULL")
                    return
                }

                Log.d("API_DEBUG", "Employees size: ${employees.size}")


                val entityList = employees.map { it.toEntity() }

                dao.replaceEmployees(entityList)

                // Insert new data
                //dao.insertEmployees(entityList)

                Log.d("API_DEBUG", "Inserted into Room successfully")

            } else {
                Log.e("API_DEBUG", "API failed: ${response.errorBody()?.string()}")
            }

        } catch (e: Exception) {
            Log.e("API_DEBUG", "Exception: ${e.message}")
        }
    }

    override fun getEmployeeById(id: String): Flow<EmployeeEntity?> {
        return dao.getEmployeeById(id)
    }
}