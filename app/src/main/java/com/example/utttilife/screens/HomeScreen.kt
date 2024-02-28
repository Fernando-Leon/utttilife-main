package com.example.utttilife.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.utttilife.components.home.CardConsulting
import com.example.utttilife.components.home.CardIcome
import com.example.utttilife.components.home.CardRecipes

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CardIcome(navController)
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            CardRecipes(navController)
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            CardConsulting(navController)
        }
        
        item { 
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}
