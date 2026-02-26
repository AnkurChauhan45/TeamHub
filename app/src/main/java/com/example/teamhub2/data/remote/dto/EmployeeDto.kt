package com.example.teamhub2.data.remote.dto

data class EmployeeDto(
    val id: String,
    val name: String,
    val designation: String,
    val department: String,
    val is_active: Boolean,
    val img_url: String?,
    val email: String,
    val city: String,
    val country: String,
    val joining_date: String
)