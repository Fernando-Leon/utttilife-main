package com.example.utttilife.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)

val listOfNavItems : List<NavItem> = listOf(
    NavItem(
        label = "Inicio",
        icon = Icons.Default.Home,
        route = Screens.HomeScreen.name
    ),
    NavItem(
        label = "Recetas",
        icon = Icons.Default.Create,
        route = Screens.RecipesScreen.name
    ),NavItem(
        label = "Rentas",
        icon = Icons.Default.LocationOn,
        route = Screens.IcomeScreen.name
    ),
    NavItem(
        label = "Chat Amigo",
        icon = Icons.Default.Call,
        route = Screens.ConsultingScreen.name
    ),
)