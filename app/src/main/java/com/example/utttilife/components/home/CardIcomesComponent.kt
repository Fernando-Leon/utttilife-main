package com.example.utttilife.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.utttilife.R
import com.example.utttilife.navigation.Screens

// Card to navigate to Icome screen
@Composable
fun CardIcome(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.house_isometric),
                    contentDescription = null,
                    modifier = Modifier
                        .width(250.dp)
                )
            }

            Text(
                text = "Encuentra",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )

            Text(
                text = "los mejores lugares",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )

            Text(
                text = "para vivir",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )


            Spacer(modifier = Modifier.height(13.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Button(
                    // Navigation to icomes screen
                    onClick = { navController.navigate(Screens.IcomeScreen.name) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Descubrir",
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