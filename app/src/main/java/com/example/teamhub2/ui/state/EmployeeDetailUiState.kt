package com.example.teamhub2.ui.state

import com.example.teamhub2.data.local.entity.EmployeeEntity

sealed class EmployeeDetailUiState {

    object Loading : EmployeeDetailUiState()

    data class Success(
        val employee: EmployeeEntity
    ) : EmployeeDetailUiState()

    data class Error(
        val message: String
    ) : EmployeeDetailUiState()
}