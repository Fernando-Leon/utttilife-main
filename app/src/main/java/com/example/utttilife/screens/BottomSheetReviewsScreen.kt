package com.example.utttilife.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.utttilife.data.Responses.ResponsesRents.Opinion
import com.example.utttilife.data.clients.OpinionsClient
import com.example.utttilife.data.services.OpinionsApiService
import kotlinx.coroutines.launch

@Composable
fun ReviewsView(id: String, opinions: List<Opinion>){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        opinions.forEach { opinion ->
            CardOpinion(opinion = opinion.text, opinion.rating.toString())
            Spacer(modifier = Modifier.height(10.dp))
        }
        PublishOpinion(id)
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

@Composable
fun PublishOpinion(id: String) {
    var opinion by remember { mutableStateOf("") }
    var ratingText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0.0) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = opinion,
            onValueChange = { opinion = it },
            label = { Text("Opinión") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = ratingText,
            onValueChange = { newValue ->
                if (newValue.isEmpty()) {
                    ratingText = ""
                    rating = 0.0
                } else if (newValue.count { it == '.' } <= 1 && newValue.all { it.isDigit() || it == '.' }) {
                    val newRating = newValue.toDoubleOrNull()
                    if (newRating != null && newRating in 1.0..5.0) {
                        ratingText = newValue
                        rating = newRating
                    }
                }
            },
            label = { Text("Calificación (1-5)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    val response = setOpinion(id = id, text = opinion, rating = rating)
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show()
                }
            },
            enabled = opinion.isNotBlank() && rating in 1.0..5.0
        ) {
            Text("Enviar Opinión")
        }
    }
}


suspend fun setOpinion(id: String, text: String, rating: Double): String {
    val request = Opinion(id, rating, text)
    val service = OpinionsClient.makeOpinionService().create(OpinionsApiService::class.java)
    return try {
        val response = service.setBuildingOpinion(request = request)
        if (response.isSuccessful) {
            val opinionResponse = response.body()
            opinionResponse?.message ?: "Opinión enviada exitosamente."
        } else {
            Log.e("Error", "Ocurrió un error durante la llamada a la API: ${response.errorBody()?.string()}")
            "Ocurrió un error al subir la opinion, intenta de nuevo mas tarde"
        }
    } catch (e: Exception) {
        Log.e("Excepción", "Excepción al enviar opinión: ${e.localizedMessage}")
        "No se pudo enviar la opinion"
    }
}