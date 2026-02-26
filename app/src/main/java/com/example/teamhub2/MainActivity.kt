package com.example.teamhub2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.teamhub2.ui.navigation.AppNavGraph
import com.example.teamhub2.ui.theme.TeamHub2Theme
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamHub2Theme {
                AppNavGraph()
            }
        }
    }
}


