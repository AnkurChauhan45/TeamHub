package com.example.teamhub2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.teamhub2.ui.navigation.AppNavGraph
import com.example.teamhub2.ui.theme.TeamHub2Theme
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.teamhub2.viewmodel.ThemeViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark by themeViewModel.isDarkMode.collectAsState()

            TeamHub2Theme(darkTheme = isDark) {
                AppNavGraph(themeViewModel)
            }
        }
    }
}


