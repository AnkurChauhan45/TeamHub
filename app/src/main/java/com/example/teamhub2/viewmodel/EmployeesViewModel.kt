package com.example.teamhub2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamhub2.data.local.entity.EmployeeEntity
import com.example.teamhub2.domain.repository.EmployeeRepository
import com.example.teamhub2.ui.state.EmployeesUiState
import com.example.teamhub2.ui.state.FilterState
import com.example.teamhub2.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val repository: EmployeeRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    // SEARCH

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    // Debounce search input
    private val debouncedSearchQuery =
        searchQuery.debounce(300)

    // FILTER

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    fun updateDepartment(department: String?) {
        _filterState.update { it.copy(selectedDepartment = department) }
    }

    fun updateDesignation(designation: String?) {
        _filterState.update { it.copy(selectedDesignation = designation) }
    }

    fun updateStatus(active: Boolean?) {
        _filterState.update { it.copy(isActive = active) }
    }

    fun clearFilters() {
        _filterState.value = FilterState()
    }


    // NETWORK STATE

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    // ---------------------------
    // DATA SOURCE
    // ---------------------------

    // Step 1 — Base employee flow
    private val employeesFlow: Flow<List<EmployeeEntity>> =
        repository.getEmployees()

    // Step 2 — Departments flow
    private val departmentsFlow =
        employeesFlow.map { employees ->
            employees
                .map { it.department }
                .distinct()
                .sorted()
        }

    // Step 3 — Designations flow
    private val designationsFlow =
        employeesFlow.map { employees ->
            employees
                .map { it.designation }
                .distinct()
                .sorted()
        }

    // Step 4 — Filter employees
    private val filteredEmployeesFlow =
        combine(
            employeesFlow,
            debouncedSearchQuery,
            filterState
        ) { employees, query, filter ->

            employees
                .filter {
                    it.name.contains(query.trim(), ignoreCase = true)
                }
                .filter {
                    filter.selectedDepartment == null ||
                            it.department.equals(filter.selectedDepartment, true)
                }
                .filter {
                    filter.selectedDesignation == null ||
                            it.designation.equals(filter.selectedDesignation, true)
                }
                .filter {
                    filter.isActive == null ||
                            it.isActive == filter.isActive
                }
                .sortedBy { it.name.lowercase() }
        }

    // Step 5 — Final UI state
    val uiState: StateFlow<EmployeesUiState> =
        combine(
            filteredEmployeesFlow,
            departmentsFlow,
            designationsFlow
        ) { employees, departments, designations ->

            EmployeesUiState.Success(
                employees = employees,
                departments = departments,
                designations = designations,
                totalCount = employees.size
            ) as EmployeesUiState
        }
            .onStart { emit(EmployeesUiState.Loading) }
            .catch { emit(EmployeesUiState.Error("Something went wrong")) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                EmployeesUiState.Loading
            )

    // ---------------------------
    // INIT
    // ---------------------------

    init {
        observeNetwork()
        loadInitialEmployees()
    }

    // ---------------------------
    // INITIAL LOAD
    // ---------------------------

    private fun loadInitialEmployees() {
        viewModelScope.launch {

            if (!_isOffline.value) {
                repository.refreshEmployees()
            }

            isInitialLoadDone = true
        }
    }

    // ---------------------------
    // PULL TO REFRESH
    // ---------------------------

    fun refreshEmployees() {
        viewModelScope.launch {

            if (_isRefreshing.value) return@launch

            _isRefreshing.value = true

            try {
                repository.refreshEmployees()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    // ---------------------------
    // NETWORK OBSERVER
    // ---------------------------

    private var isInitialLoadDone = false

    private fun observeNetwork() {
        viewModelScope.launch {

            networkUtils.observeNetwork()
                .drop(1)
                .distinctUntilChanged()
                .collect { isConnected ->

                    _isOffline.value = !isConnected

                    if (isConnected && isInitialLoadDone) {
                        refreshEmployees()
                    }
                }
        }
    }
}