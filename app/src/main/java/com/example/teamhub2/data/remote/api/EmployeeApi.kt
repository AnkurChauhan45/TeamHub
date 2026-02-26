package com.example.teamhub2.data.remote.api

import com.example.teamhub2.data.remote.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface EmployeeApi {

    @GET("employees")
    suspend fun getEmployees(): Response<ApiResponse>
}