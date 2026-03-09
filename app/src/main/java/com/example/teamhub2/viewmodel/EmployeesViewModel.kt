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



    //  SEARCH
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    //  FILTER

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

    //  NETWORK STATE

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    //  DATA SOURCE

    private val rawEmployeesFlow: Flow<List<EmployeeEntity>> =
        repository.getEmployees()

    //  UI STATE
    val uiState: StateFlow<EmployeesUiState> =
        combine(
            rawEmployeesFlow,
            searchQuery,
            filterState
        ) { allEmployees, query, filter ->

            val departments = allEmployees
                .map { it.department }
                .distinct()
                .sorted()

            val designations = allEmployees
                .map { it.designation }
                .distinct()
                .sorted()

            val filteredEmployees = allEmployees
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
                .sortedWith { a, b ->

                    val regex = Regex("""(\D+)\s*(\d+)?""")

                    val aMatch = regex.find(a.name.lowercase())
                    val bMatch = regex.find(b.name.lowercase())

                    val aText = aMatch?.groupValues?.get(1) ?: ""
                    val bText = bMatch?.groupValues?.get(1) ?: ""

                    val textCompare = aText.compareTo(bText)

                    if (textCompare != 0) {
                        textCompare
                    } else {
                        val aNum = aMatch?.groupValues?.getOrNull(2)?.toIntOrNull() ?: 0
                        val bNum = bMatch?.groupValues?.getOrNull(2)?.toIntOrNull() ?: 0
                        aNum.compareTo(bNum)
                    }
                }
            //  explicitly return EmployeesUiState
            EmployeesUiState.Success(
                employees = filteredEmployees,
                departments = departments,
                designations = designations,
                totalCount = filteredEmployees.size
            ) as EmployeesUiState

        }
            .onStart { emit(EmployeesUiState.Loading) }
            .catch { emit(EmployeesUiState.Error("Something went wrong")) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                EmployeesUiState.Loading
            )

    //  INIT

    init {
        observeNetwork()
        loadInitialEmployees()
    }

    //  INITIAL LOAD


private fun loadInitialEmployees() {
    viewModelScope.launch {
        if (!_isOffline.value) {
            repository.refreshEmployees()
        }
    }
}

    //  PULL TO REFRESH

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

    // NETWORK OBSERVER

    private fun observeNetwork() {
        viewModelScope.launch {

            networkUtils.observeNetwork()
                .drop(1) // ignore first emission
                .distinctUntilChanged()
                .collect { isConnected ->

                    _isOffline.value = !isConnected

                    if (isConnected) {
                        refreshEmployees()
                    }
                }
        }
    }

//    private fun observeNetwork() {
//        viewModelScope.launch {
//
//            networkUtils.observeNetwork()
//                .distinctUntilChanged()
//                .collect { isConnected ->
//
//                    _isOffline.value = !isConnected
//
//                    // Internet available → auto refresh API
//                    if (isConnected) {
//                        refreshEmployees()
//                    }
//                }
//        }
//    }
}