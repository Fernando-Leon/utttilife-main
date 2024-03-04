package com.example.utttilife.components.recipes

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Send image
@Composable
fun ContentSend(selectedImageBitmap: Bitmap?, response: MutableState<String>) {
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        if (!loading) {
            IconButton(
                onClick = {
                    loading = true
                    try {
                        selectedImageBitmap?.let {
                            scope.launch {
                                response.value = getResponse(it)
                                loading = false
                            }
                        }
                    } catch (e: Exception) {
                        println("Ocurrio un error en: ${e.message}")
                    }
                },
                enabled = !loading,
            ) {
                Icon(
                    Icons.Filled.Send,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    contentDescription = "Enviar",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (loading) {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }


}
