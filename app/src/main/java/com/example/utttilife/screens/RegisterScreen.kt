package com.example.utttilife.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@Preview
@Composable
fun RegisterScreen() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Register") {
        composable("Register") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .border(0.dp, Color.White, shape = RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        item {
                            RegisterComponent(onReisterSuccess = {
                                navController.navigate("home") {
                                    popUpTo("Register") { inclusive = true } // Esto elimina la pantalla de login del stack de navegación
                                }
                            }, onLoginSucess = {
                                navController.navigate("login") {
                                    popUpTo("Register") { inclusive = true } // Esto elimina la pantalla de login del stack de navegación
                                }
                            })
                        }
                    }
                }
            }
        }
        composable("home"){
            MainScreen()
        }
        composable("login"){
            LoginScreen()
        }
    }


}



