package com.example.teamhub2.ui.employees

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teamhub2.ui.state.FilterState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    departments: List<String>,
    designations: List<String>,
    currentFilter: FilterState,
    onDepartmentSelected: (String?) -> Unit,
    onDesignationSelected: (String?) -> Unit,
    onStatusSelected: (Boolean?) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Filter Employees",
                    style = MaterialTheme.typography.headlineSmall
                )

                TextButton(onClick = onClearAll) {
                    Text("Clear All")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---------------- DEPARTMENT ----------------

            if (departments.isNotEmpty()) {

                Text(
                    text = "Department",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    departments.forEach { department ->

                        FilterChip(
                            selected = currentFilter.selectedDepartment == department,
                            onClick = {
                                onDepartmentSelected(
                                    if (currentFilter.selectedDepartment == department)
                                        null else department
                                )
                            },
                            label = { Text(department) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            //DESIGNATION

            if (designations.isNotEmpty()) {

                Text(
                    text = "Designation",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    designations.forEach { designation ->

                        FilterChip(
                            selected = currentFilter.selectedDesignation == designation,
                            onClick = {
                                onDesignationSelected(
                                    if (currentFilter.selectedDesignation == designation)
                                        null else designation
                                )
                            },
                            label = { Text(designation) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            //STATUS

            Text(
                text = "Status",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FilterChip(
                    selected = currentFilter.isActive == true,
                    onClick = {
                        onStatusSelected(
                            if (currentFilter.isActive == true) null else true
                        )
                    },
                    label = { Text("Active") }
                )

                FilterChip(
                    selected = currentFilter.isActive == false,
                    onClick = {
                        onStatusSelected(
                            if (currentFilter.isActive == false) null else false
                        )
                    },
                    label = { Text("Inactive") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            //APPLY BUTTON
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filters")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}