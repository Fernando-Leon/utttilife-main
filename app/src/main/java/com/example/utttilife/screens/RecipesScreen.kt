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

// Componente principal de la pantalla
@Composable
fun RecipesScreen() {
    // Obtenemos la instancia del ViewModel
    val viewModel: MainViewModel = viewModel()
    // Definimos un contenedor Box
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Guardamos el NavController
        val navController = rememberNavController()
        // Configuramos el sistema de navegación
        NavHost(navController = navController, startDestination = "main") {
            // Definimos el destino para la pantalla principal
            composable("main") { LayoutContent(navController, viewModel) }
            // Definimos el destino para la pantalla de la cámara
            composable("camera") {
                // Llamamos al componente CameraImage y le pasamos el NavController, la función de captura de imagen del ViewModel y el contexto actual
                CameraImage(navController, viewModel::onImageCaptured, context = LocalContext.current)
            }
        }
    }
}
