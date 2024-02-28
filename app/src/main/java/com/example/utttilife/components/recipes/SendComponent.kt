package com.example.utttilife.components.recipes

import android.graphics.Bitmap
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Send image
@Composable
fun ContentSend(selectedImageBitmap: Bitmap?, response: MutableState<String>) {
    val scope = rememberCoroutineScope()

    IconButton(onClick = {
        // Request to openIA
        try {
            selectedImageBitmap?.let {
                scope.launch {
                    response.value = getResponse(it)
                }
            }
        } catch (e: Exception){
            println("Ocurrio un error en: ${e.message}")
        }
    })
    {
        Icon(
            Icons.Filled.Send,
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            contentDescription = "Enviar",
            modifier = Modifier.size(24.dp)
        )
    }
}