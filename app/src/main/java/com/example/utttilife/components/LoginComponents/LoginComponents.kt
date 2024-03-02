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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.utttilife.data.Responses.ResponsesLogin.BodyLoginResponse
import com.example.utttilife.data.clients.RetrofitClientLogin
import kotlinx.coroutines.launch


@Composable
fun LoginComponents(onLoginSuccess: () -> Unit,onRegisterClicked: () -> Unit) {

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, Color.White, shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), RoundedCornerShape(10.dp))

            ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(30.dp))
                Text("Bienvenido", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color=MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(1.dp))
                Text("Inicio de sesion", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color= MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(30.dp))
                val textValueUser= remember{
                    mutableStateOf("")
                }
                OutlinedTextField(
                    value = textValueUser.value,
                    onValueChange = { textValueUser.value=it},
                    label = { Text("Nombre de Usuario") },
                    placeholder = { Text("Ej.Arturo1754") },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                val textValuePassword = remember{
                    mutableStateOf("")
                }
                OutlinedTextField(
                    value = textValuePassword.value,
                    onValueChange = { textValuePassword.value =it},
                    label = { Text("Contraseña") },
                    placeholder = { Text("**********") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("¿No tienes un perfil?", color=MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, style =
                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text("Crea uno, solo te llevara un instante", color=MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, style =
                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Alinea los elementos horizontalmente
                ) {
                    Button(
                        onClick = { onRegisterClicked() }, // Llama al callback cuando se hace clic en el botón
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Registrarme")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(

                        onClick = {

                            // Comprueba que los términos y condiciones están aceptados

                            // Comprueba que los campos no estén vacíos
                            if (textValueUser.value.isEmpty() || textValuePassword.value.isEmpty()) {
                                // Mostrar mensaje de error sobre campos vacíos
                                return@OutlinedButton
                            }
                            scope.launch {
                                try {
                                    val loginRequest = BodyLoginResponse(textValueUser.value, textValuePassword.value)
                                    val userResponse = RetrofitClientLogin.create().validateUser(loginRequest)
                                    if (userResponse.exist) {
                                        // El usuario existe, realiza la navegación
                                        onLoginSuccess()
                                    } else {
                                        // Mostrar mensaje de usuario no existente
                                    }
                                } catch (e: Exception) {
                                    // Manejar el error, por ejemplo, mostrando un mensaje al usuario
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Iniciar Sesión")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

