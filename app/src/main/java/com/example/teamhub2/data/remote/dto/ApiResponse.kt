package com.example.teamhub2.data.remote.dto

data class ApiResponse(
    val status: String,
    val message: String,
    val data: EmployeeDataDto,
    val meta: MetaDto
)