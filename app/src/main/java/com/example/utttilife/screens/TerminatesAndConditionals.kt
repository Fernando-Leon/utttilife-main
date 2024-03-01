package com.example.utttilife.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminatesAndConditionals(onBackPressed: () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Política de privacidad") },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
        Box(
            modifier = Modifier.fillMaxSize().
            padding(top = 65.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Somos muy conscientes de lo importante que es la privacidad para usted. Con el propósito de describir en detalle nuestro proceso de recolección de información personal, hemos revisado la Política de privacidad minuciosamente de conformidad con las leyes y normativas más recientes. Al hacer clic en Acepto, reconoce que ha leído, comprendido y aceptado completamente todos los contenidos de la Política de privacidad de. Por favor, dedique un momento a leer la Política de privacidad. Si tiene alguna pregunta, no dude en ponerse en contacto con nosotros.",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
