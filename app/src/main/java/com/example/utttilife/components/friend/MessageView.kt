package com.example.utttilife.components.friend

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageView(message: String, isUserMessage: Boolean) {
    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = message,
            modifier = Modifier.align(if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart)
        )
    }
}
