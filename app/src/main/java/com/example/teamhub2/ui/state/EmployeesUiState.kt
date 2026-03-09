package com.example.teamhub2.ui.state

import com.example.teamhub2.data.local.entity.EmployeeEntity

sealed class EmployeesUiState {

    object Loading : EmployeesUiState()

    data class Success(
        val employees: List<EmployeeEntity>,
        val departments: List<String>,
        val designations: List<String>,
        val totalCount: Int
    ) : EmployeesUiState()

    data class Error(val message: String) : EmployeesUiState()
}