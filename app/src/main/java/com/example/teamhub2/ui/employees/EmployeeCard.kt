package com.example.teamhub2.ui.employees


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.teamhub2.data.local.entity.EmployeeEntity

@Composable
fun EmployeeCard(
    employee: EmployeeEntity,
    onClick: () -> Unit
) {

    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 1.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 1.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Profile Image with soft background ring
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = employee.imgUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Employee Info
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = employee.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = employee.designation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${employee.city}, ${employee.country}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            StatusChip(employee.isActive)
        }
    }
}
@Composable
fun StatusChip(isActive: Boolean) {

    val background =
        if (isActive)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else
            MaterialTheme.colorScheme.error.copy(alpha = 0.12f)

    val content =
        if (isActive)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.error

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = if (isActive) "Active" else "Inactive",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = content
        )
    }
}