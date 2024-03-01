package com.example.utttilife.data.Responses.ResponsesLogin

data class UserResponse(
    val message: String,
    val token: String,
    val exist: Boolean
)