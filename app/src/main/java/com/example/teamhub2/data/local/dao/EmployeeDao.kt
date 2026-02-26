package com.example.teamhub2.data.local.dao

import androidx.room.*
import com.example.teamhub2.data.local.entity.EmployeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employees: List<EmployeeEntity>)

    @Query("DELETE FROM employees")
    suspend fun clearEmployees()

    @Query("SELECT * FROM employees")
    fun getEmployees(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM employees WHERE id = :id")
    fun getEmployeeById(id: String): Flow<EmployeeEntity?>

}