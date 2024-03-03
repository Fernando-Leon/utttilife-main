package com.example.utttilife.data.Responses.ResponsesRents

data class ApartmentsResponse(
    val _id: String,
    val coordinates: List<Double>,
    val cost: Int,
    val description: String,
    val rules: String,
    val type: String,
    val imageUrl: List<String>
)