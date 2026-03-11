package com.example.teamhub2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatusChip(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    small: Boolean = false
) {

    val textStyle =
        if (small) MaterialTheme.typography.labelSmall
        else MaterialTheme.typography.labelMedium

    val dotSize = if (small) 6.dp else 8.dp
    val horizontalPadding = if (small) 8.dp else 12.dp
    val verticalPadding = if (small) 3.dp else 6.dp

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (isActive) Color(0xFFE8F5E9)
                else Color(0xFFFFEBEE)
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(
                        if (isActive) Color(0xFF4CAF50)
                        else Color(0xFFE53935)
                    )
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = if (isActive) "Active" else "Inactive",
                style = textStyle,
                fontWeight = FontWeight.SemiBold,
                color =
                    if (isActive) Color(0xFF2E7D32)
                    else Color(0xFFC62828)
            )
        }
    }
}