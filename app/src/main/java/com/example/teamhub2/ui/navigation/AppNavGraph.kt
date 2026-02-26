package com.example.teamhub2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.teamhub2.ui.employees.EmployeesScreen
import com.example.teamhub2.ui.detail.EmployeeDetailScreen


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Employees.route
    ) {

        composable(Screen.Employees.route) {

            EmployeesScreen(
                onEmployeeClick = { employeeId ->
                    navController.navigate(
                        Screen.Detail.createRoute(employeeId)
                    )
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) {

            EmployeeDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}