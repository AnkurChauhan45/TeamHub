package com.example.teamhub2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileAvatar(
    name: String,
    imageUrl: String?,
    size: Int = 56
) {

    val initials = getInitials(name)

    val painter = rememberAsyncImagePainter(model = imageUrl)
    val state = painter.state

    when (state) {

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = name,
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
            )
        }

        is AsyncImagePainter.State.Error -> {
            InitialAvatar(initials, size)
        }

        else -> {
            // while loading show image container
            Image(
                painter = painter,
                contentDescription = name,
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
            )
        }
    }
}
@Composable
fun InitialAvatar(initials: String, size: Int) {

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

fun getInitials(name: String): String {

    val parts = name.trim().split(" ")

    return when (parts.size) {
        0 -> ""
        1 -> parts[0].take(1).uppercase()
        else -> (parts.first().take(1) + parts.last().take(1)).uppercase()
    }
}