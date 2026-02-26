package com.example.teamhub2.ui.employees

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teamhub2.viewmodel.EmployeesViewModel
import com.example.teamhub2.ui.state.EmployeesUiState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EmployeesScreen(
//    viewModel: EmployeesViewModel = hiltViewModel(),
//    onEmployeeClick: (String) -> Unit
//) {
//
//    val state by viewModel.uiState.collectAsState()
//    val isRefreshing by viewModel.isRefreshing.collectAsState()
//    val isOffline by viewModel.isOffline.collectAsState()
//    val filterState by viewModel.filterState.collectAsState()
//    val searchQuery by viewModel.searchQuery.collectAsState()
//
//    var showFilterSheet by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Employees") },
//                actions = {
//
//                    // 🔄 Manual Refresh Button
//                    IconButton(onClick = { viewModel.refreshEmployees() }) {
//                        Icon(
//                            imageVector = Icons.Default.Refresh,
//                            contentDescription = "Refresh"
//                        )
//                    }
//
//                    // 🔍 Filter Button
//                    IconButton(onClick = { showFilterSheet = true }) {
//                        Icon(
//                            imageVector = Icons.Default.FilterList,
//                            contentDescription = "Filter"
//                        )
//                    }
//                }
//            )
//        }
//    ) { padding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//
//            // 🔎 SEARCH FIELD
//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = viewModel::onSearchChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                placeholder = { Text("Search by name") },
//                singleLine = true
//            )
//
//            // 📡 OFFLINE BANNER
//            if (isOffline) {
//                Surface(
//                    color = MaterialTheme.colorScheme.errorContainer,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(
//                        text = "You're offline. Showing cached data.",
//                        modifier = Modifier.padding(12.dp),
//                        color = MaterialTheme.colorScheme.onErrorContainer
//                    )
//                }
//            }
//
//            when (val currentState = state) {
//
//                is EmployeesUiState.Loading -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                }
//
//                is EmployeesUiState.Error -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(currentState.message)
//                    }
//                }
//
//                is EmployeesUiState.Success -> {
//
//                    if (currentState.employees.isEmpty()) {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text("No employees found")
//                        }
//                    } else {
//
//                        LazyColumn(
//                            modifier = Modifier.fillMaxSize()
//                        ) {
//                            items(currentState.employees) { employee ->
//                                EmployeeCard(
//                                    employee = employee,
//                                    onClick = {
//                                        onEmployeeClick(employee.id)
//                                    }
//                                )
//                            }
//                        }
//                    }
//
//                    // 🔽 FILTER SHEET
//                    if (showFilterSheet) {
//                        FilterBottomSheet(
//                            departments = currentState.departments,
//                            designations = currentState.designations,
//                            currentFilter = filterState,
//                            onDepartmentSelected = viewModel::updateDepartment,
//                            onDesignationSelected = viewModel::updateDesignation,
//                            onStatusSelected = viewModel::updateStatus,
//                            onClearAll = viewModel::clearFilters,
//                            onDismiss = { showFilterSheet = false }
//                        )
//                    }
//                }
//            }
//        }
//
//        // 🔄 Small loading overlay while refreshing
//        if (isRefreshing) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//    }
//}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EmployeesScreen(
    viewModel: EmployeesViewModel = hiltViewModel(),
    onEmployeeClick: (String) -> Unit
) {

    val state by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isOffline by viewModel.isOffline.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }

    // ✅ Pull Refresh State
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshEmployees() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Employees") },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(pullRefreshState)
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                // 🔎 Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search by name") },
                    singleLine = true
                )

                // 📡 Offline Banner
                if (isOffline) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "You're offline. Showing cached data.",
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                when (val currentState = state) {

                    is EmployeesUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is EmployeesUiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(currentState.message)
                        }
                    }

                    is EmployeesUiState.Success -> {

                        if (currentState.employees.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No employees found")
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    items = currentState.employees,
                                    key = { it.id }
                                ) { employee ->
                                    EmployeeCard(
                                        employee = employee,
                                        onClick = {
                                            onEmployeeClick(employee.id)
                                        }
                                    )
                                }
                            }
                        }

                        if (showFilterSheet) {
                            FilterBottomSheet(
                                departments = currentState.departments,
                                designations = currentState.designations,
                                currentFilter = filterState,
                                onDepartmentSelected = viewModel::updateDepartment,
                                onDesignationSelected = viewModel::updateDesignation,
                                onStatusSelected = viewModel::updateStatus,
                                onClearAll = viewModel::clearFilters,
                                onDismiss = { showFilterSheet = false }
                            )
                        }
                    }
                }
            }

            // 🔽 Pull Refresh Indicator
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}