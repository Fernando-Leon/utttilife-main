package com.example.utttilife.data.Responses.ResponsesRents

data class BuildingsResponse(
    val _id: String,
    val address: String,
    val coordinates: List<Double>,
    val name: String,
    val opinions: List<Opinion>,
    val tenant: Tenant
)