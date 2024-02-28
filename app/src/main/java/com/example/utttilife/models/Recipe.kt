package com.example.utttilife.models

import androidx.compose.ui.graphics.ImageBitmap

data class Recipe(
    val title: String,
    val image: ImageBitmap,
    val ingredients: String,
    val preparation: String,
    val nutritionalInfo: String
)