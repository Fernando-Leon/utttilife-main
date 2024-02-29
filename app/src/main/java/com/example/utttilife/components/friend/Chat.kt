package com.example.utttilife.components.friend

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Chat() {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Message(menssaje = "Como se llama esa persona?")    
    }
}