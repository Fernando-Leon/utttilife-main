package com.example.utttilife.data.Responses.ResponsesUsers

data class BodyUserRegisterResponse (
    val username: String,
    val name: String,
    val password: String,
    val email: String,
    val phone: String
)