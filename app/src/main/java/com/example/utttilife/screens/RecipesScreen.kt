package com.example.utttilife.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.utttilife.components.recipes.MainViewModel
import com.example.utttilife.views.recipes.CameraImage
import com.example.utttilife.views.recipes.LayoutContent

@Composable
fun RecipesScreen() {
    val viewModel: MainViewModel = viewModel()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "main") {
            composable("main") { LayoutContent(navController, viewModel) }
            composable("camera") {
                CameraImage(navController, viewModel::onImageCaptured, context = LocalContext.current)
            }
        }
    }
}