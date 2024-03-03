package com.example.utttilife.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.utttilife.components.rents.Map
import com.example.utttilife.components.rents.SearchBar

@Composable
fun IcomeScreen() {
    val navController = rememberNavController()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        NavHost(navController = navController, startDestination = "map") {
            composable("map") { Map(navController = navController) }
            composable(
                "departmentDetails/{type}/{description}/{cost}/{rules}/{encodedListJsonString}",
                arguments = listOf(
                    navArgument("type") { type = NavType.StringType },
                    navArgument("description") { type = NavType.StringType },
                    navArgument("cost") { type = NavType.StringType },
                    navArgument("rules") { type = NavType.StringType },
                    navArgument("encodedListJsonString") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                DepartmentDetailsScreen(
                    navController = navController,
                    type = backStackEntry.arguments?.getString("type") ?: "",
                    description = backStackEntry.arguments?.getString("description") ?: "",
                    cost = backStackEntry.arguments?.getString("cost") ?: "",
                    rules = backStackEntry.arguments?.getString("rules") ?: "",
                    encodedListJsonString = backStackEntry.arguments?.getString("encodedListJsonString") ?: ""
                )
            }
        }
    }
}