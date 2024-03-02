package com.example.utttilife.components.home

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.utttilife.R
import com.example.utttilife.navigation.Screens

@Composable
fun CardAdd(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 16.dp, end = 16.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.house_isometric),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.2f),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = "¿Quieres anunciar",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "tus departamentos?",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "💸",
                style = MaterialTheme.typography.titleLarge,
            )
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                OutlinedButton(
                    // Navigation to consulting screen
                    onClick = { navController.navigate(Screens.RegisterApartament.name) },
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