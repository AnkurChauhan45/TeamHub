package com.example.teamhub2.ui.employees

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import com.example.teamhub2.viewmodel.ThemeViewModel
import androidx.compose.material.icons.filled.SearchOff

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EmployeesScreen(
    themeViewModel: ThemeViewModel,
    viewModel: EmployeesViewModel = hiltViewModel(),
    onEmployeeClick: (String) -> Unit
) {

    val state by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isOffline by viewModel.isOffline.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Dark mode state
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshEmployees() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Employees") },
                actions = {

                    //  Dark Mode Toggle
                    IconButton(
                        onClick = { themeViewModel.toggleTheme() }
                    ) {
                        Icon(
                            imageVector =
                                if (isDarkMode) Icons.Default.LightMode
                                else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }

                    // Filter Button
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

                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search by name") },
                    singleLine = true
                )

                // Offline Banner
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

                        Text(
                            text = "Total: ${currentState.totalCount}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (currentState.employees.isEmpty()) {
                            EmptyState()

                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
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

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun EmptyState() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No employees found",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Try adjusting your search or filters",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}