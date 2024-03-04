package com.example.utttilife.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.example.utttilife.data.services.ApartmentsApiService
import com.example.utttilife.data.Responses.ResponsesRents.ApartmentsRequest
import com.example.utttilife.data.clients.ApartmentsClient
import com.example.utttilife.R.*
import com.example.utttilife.components.rents.AnimatedSearchingText
import kotlinx.coroutines.delay

data class Apartments(val type: String, val description: String, val cost: Int, val rules: String, val imageUrl: List<String>)

@Composable
fun DepartmentsView(navController: NavHostController, coordinates: List<Double>) {
    var apartmentsMarker by remember { mutableStateOf<List<Apartments>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val fetchedApartments = obtainApartments(coordinates)
        apartmentsMarker = fetchedApartments
        isLoading = false
    }

    if (isLoading) {
        AnimatedSearchingApartmentsText()
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            apartmentsMarker?.forEach { apartment ->
                CardApartments(navController = navController, type = apartment.type, description = apartment.description, cost = apartment.cost.toString(), rules = apartment.rules, imageUrl = apartment.imageUrl)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun CardApartments(navController: NavHostController, type: String, description: String, cost: String, rules: String, imageUrl: List<String>){
    val gson = Gson()
    val listJsonString = gson.toJson(imageUrl)
    val encodedListJsonString = Uri.encode(listJsonString)
    val typeEncoded = Uri.encode(type)
    val descriptionEncoded = Uri.encode(description)
    val costEncoded = Uri.encode(cost)
    val rulesEncoded = Uri.encode(rules)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(5.dp)
    ){
        Row(
            modifier = Modifier.padding(5.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.55f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Tipo: ",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = type,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Costo al mes: ",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = cost,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedButton(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    onClick = { navController.navigate("departmentDetails/$typeEncoded/$descriptionEncoded/$costEncoded/$rulesEncoded/$encodedListJsonString")
                }) {
                    Icon(painterResource(id = drawable.read_more), contentDescription = "")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Conoce más")
                }
            }
        }
    }
}

suspend fun obtainApartments(coordinates: List<Double>): List<Apartments> {
    val request = ApartmentsRequest(coordinates)
    val service = ApartmentsClient.makeApartmentsService().create(ApartmentsApiService::class.java)
    try {
        val response = service.getApartmentsCoordinates(request = request)
        return if (response.isSuccessful) {
            val apartmentsList = response.body() ?: emptyList()
            apartmentsList.map { apartment ->
                Apartments(
                    type = apartment.type,
                    description = apartment.description,
                    cost = apartment.cost,
                    rules = apartment.rules,
                    imageUrl = apartment.imageUrl
                )
            }
        } else {
            println("Erro durante la llamada a la API: ${response.errorBody()?.string()}")
            emptyList()
        }
    } catch (e: Exception) {
        println("Exception occurred during the API call: ${e.message}")
        return emptyList()
    }
}

@Composable
fun AnimatedSearchingApartmentsText() {
    val baseText = "Buscando"
    var dotsCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(500L)
            dotsCount = (dotsCount + 1) % 4
        }
    }

    Text(
        text = "$baseText${".".repeat(dotsCount)}",
        style = MaterialTheme.typography.titleMedium
    )
}