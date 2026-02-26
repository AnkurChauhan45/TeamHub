package com.example.teamhub2.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamhub2.domain.repository.EmployeeRepository
import com.example.teamhub2.ui.state.EmployeeDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EmployeeDetailViewModel @Inject constructor(
    private val repository: EmployeeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val employeeId: String =
        savedStateHandle["id"] ?: ""

    val uiState: StateFlow<EmployeeDetailUiState> =
        repository.getEmployeeById(employeeId)
            .map { employee ->
                if (employee != null) {
                    EmployeeDetailUiState.Success(employee)
                } else {
                    EmployeeDetailUiState.Error("Employee not found")
                }
            }
            .catch {
                emit(EmployeeDetailUiState.Error("Something went wrong"))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                EmployeeDetailUiState.Loading
            )
}