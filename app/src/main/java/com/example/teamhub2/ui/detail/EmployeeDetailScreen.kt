package com.example.teamhub2.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.teamhub2.ui.components.ProfileAvatar
import com.example.teamhub2.ui.components.StatusChip
import com.example.teamhub2.ui.state.EmployeeDetailUiState
import com.example.teamhub2.viewmodel.EmployeeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailScreen(
    onBack: () -> Unit,
    viewModel: EmployeeDetailViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Employee Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        when (val state = uiState) {

            is EmployeeDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is EmployeeDetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message)
                }
            }

            is EmployeeDetailUiState.Success -> {

                val employee = state.employee

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            )
                    )

                    // Profile image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        ProfileAvatar(
                            name = employee.name,
                            imageUrl = employee.imgUrl,
                            size = 120
                        )

//                        AsyncImage(
//                            model = employee.imgUrl,
//                            contentDescription = null,
//                            modifier = Modifier
//                                .size(120.dp)
//                                .offset(y = (-60).dp)
//                                .clip(CircleShape)
//                                .border(
//                                    4.dp,
//                                    MaterialTheme.colorScheme.background,
//                                    CircleShape
//                                )
//                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Name
                    Text(
                        text = employee.name,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status chip
                    StatusChip(
                        isActive = employee.isActive,
                        small = true,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Info card
                    ElevatedCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.elevatedCardElevation(2.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {

                            InfoRow(
                                icon = Icons.Default.Engineering,
                                label = "Designation",
                                value = employee.designation
                            )

                            InfoRow(
                                icon = Icons.Default.Business,
                                label = "Department",
                                value = employee.department
                            )

                            InfoRow(
                                icon = Icons.Default.Email,
                                label = "Email",
                                value = employee.email
                            )

                            InfoRow(
                                icon = Icons.Default.LocationOn,
                                label = "Location",
                                value = "${employee.city}, ${employee.country}"
                            )

                            InfoRow(
                                icon = Icons.Default.CalendarMonth,
                                label = "Joined",
                                value = employee.joiningDate
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {

//            Text(
//                text = label,
//                style = MaterialTheme.typography.labelMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

