package com.example.teamhub2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.teamhub2.data.local.dao.EmployeeDao
import com.example.teamhub2.data.local.entity.EmployeeEntity

@Database(
    entities = [EmployeeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TeamHubDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
}