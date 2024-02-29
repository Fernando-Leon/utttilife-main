package com.example.utttilife.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign


@Composable
fun RegisterComponent() {

    val buttonColor = Color(0xFF02A787) // Color del botón 'Registrarme' y del borde del botón 'Iniciar Sesión'
    val textColorButton = Color(0xFFF4F7F6)
    val buttonShape = RoundedCornerShape(4.dp) // Forma redondeada para el borde y el botón
    val backgrondColor = Color(0x00FFFFFF)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(0.dp, Color.White, shape = RoundedCornerShape(10.dp))
                .background(backgrondColor, RoundedCornerShape(10.dp))


        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(56.dp))
                Text("Registro", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(36.dp))
                val textValueUser= remember{
                    mutableStateOf("")
                }
                TextField(
                    value = textValueUser.value,
                    onValueChange = { textValueUser.value=it},
                    placeholder = { Text("Crea un nombre de usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp))
                    )
                Spacer(modifier = Modifier.height(16.dp))
                val textValueNombre= remember{
                    mutableStateOf("")
                }
                TextField(
                    value = textValueNombre.value,
                    onValueChange = { textValueNombre.value=it},
                    placeholder = { Text("Nombre Completo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                val textValueEmail= remember{
                    mutableStateOf("")
                }
                TextField(
                    value = textValueEmail.value,
                    onValueChange = { textValueEmail.value=it},
                    placeholder = { Text("Correo Electronico") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp))

                    )

                Spacer(modifier = Modifier.height(16.dp))
                val textValuePassword= remember{
                    mutableStateOf("")
                }
                TextField(
                    value = textValuePassword.value,
                    onValueChange = { textValuePassword.value =it},
                    placeholder = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp)),
                )
                Spacer(modifier = Modifier.height(16  .dp))
                val textValuePasswordRepeat= remember{
                    mutableStateOf("")
                }
                TextField(
                    value = textValuePasswordRepeat.value,
                    onValueChange = { textValuePasswordRepeat.value =it},
                    placeholder = { Text("Repite la Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp)),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},

                        )
                    Text(
                        "Aceptar términos y condiciones de servicio",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        style =
                        MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Alinea los elementos horizontalmente
                ) {
                    Button(
                        onClick = { /* TODO: Implement registration logic */ },
                        modifier = Modifier.weight(1f),
                        shape = buttonShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Registrarme")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                }
            }
        }
    }
}
