package com.example.utttilife.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.utttilife.data.Responses.ResponsesRents.Opinion

@Composable
fun ReviewsView(opinions: List<Opinion>){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        opinions.forEach { opinion ->
            CardOpinion(opinion = opinion.text, opinion.rating.toString())
            Spacer(modifier = Modifier.height(10.dp))
        }
        //PublishOpinion(onSubmit = )
    }
}

@Composable
fun CardOpinion(opinion: String, rating: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ){
        Row {
            Column(
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Text(
                    text = "Opinion: ",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = opinion,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = rating)
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    Icons.Filled.Star,
                    tint = Color.Blue,
                    contentDescription = "Icono de opinion",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}