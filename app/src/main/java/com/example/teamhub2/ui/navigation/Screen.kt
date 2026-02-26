package com.example.teamhub2.ui.navigation


sealed class Screen(val route: String) {

    object Employees : Screen("employees")

    object Detail : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
}