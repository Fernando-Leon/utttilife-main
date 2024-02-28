package com.example.utttilife.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.utttilife.R
import com.example.utttilife.navigation.Screens

// Card to navigate to Consulting screen
@Composable
fun CardConsulting(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ){
        Column {
            Text(
                text = "¿Necesitas ayuda",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "con tu tarea?",
                style = MaterialTheme.typography.titleLarge,
            )
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                OutlinedButton(
                    // Navigation to consulting screen
                    onClick = { navController.navigate(Screens.ConsultingScreen.name) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Conocer",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_continue_next),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }

        }
    }
}