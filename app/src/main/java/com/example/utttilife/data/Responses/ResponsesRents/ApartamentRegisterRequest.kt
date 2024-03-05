package com.example.utttilife.data.Responses.ResponsesRents
class ApartamentRegisterRequest (
    val coordinates: List<Double>,
    val name: String,
    val address: String,
    val type: String,
    val description: String,
    val cost: Int,
    val rules: String,
    val ImageUrl: List<String>
)