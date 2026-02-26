package com.example.teamhub2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeeEntity(

    @PrimaryKey
    val id: String,

    val name: String,
    val designation: String,
    val department: String,

    val isActive: Boolean,

    val imgUrl: String?,
    val email: String,

    val city: String,
    val country: String,

    val joiningDate: String
)