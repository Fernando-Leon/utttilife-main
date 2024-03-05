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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.utttilife.components.SharedPreferencesManager.SharedPreferencesManager
import com.example.utttilife.data.Responses.ResponsesLogin.BodyLoginResponse
import com.example.utttilife.data.clients.RetrofitClientLogin
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComponents(onLoginSuccess: () -> Unit,onRegisterClicked: () -> Unit) {


    // Estado del Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Alcance de Coroutine
    val scope = rememberCoroutineScope()

    // Estado mutable para errores de usuario y contraseña
    val (userError, setUserError) = remember { mutableStateOf(false) }
    val (passwordError, setPasswordError) = remember { mutableStateOf(false) }


    // Estado mutable para almacenar los datos del usuario
    val (username, setUsername) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    val (userExists, setUserExists) = remember { mutableStateOf(false) }


    // Contenedor principal
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

                // Separador
                Spacer(modifier = Modifier.height(30.dp))

                // Texto de bienvenida
                Text("Bienvenido", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color=MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(1.dp))

                // Texto de inicio de sesión
                Text("Inicio de sesion", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color= MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(30.dp))

                // Campo de texto para el nombre de usuario
                val textValueUser= remember{ mutableStateOf("") }
                OutlinedTextField(
                    value = textValueUser.value,
                    onValueChange = {
                        textValueUser.value = it
                        setUserError(false) // Restablecer el estado de error de usuario cuando el usuario cambia el texto
                    },
                    label = { Text("Nombre de Usuario") },
                    placeholder = {
                        if (userError) {
                            Text("Usuario incorrecto", color = Color.Red)
                        } else {
                            Text("Ej.Arturo1754")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = userError, // Cambiar el color del contorno basado en el estado de error de usuario
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (userError) Color.Red else MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = if (userError) Color.Red else Color.Gray
                    ),
                    singleLine = true
                )


                // Separador
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de texto para la contraseña
                val textValuePassword = remember{ mutableStateOf("") }
                OutlinedTextField(
                    value = textValuePassword.value,
                    onValueChange = {
                        textValuePassword.value = it
                        setPasswordError(false) // Restablecer el estado de error de contraseña cuando el usuario cambia el texto
                    },
                    label = { Text("Contraseña") },
                    placeholder = {
                        if (passwordError) {
                            Text("Contraseña incorrecta", color = Color.Red)
                        } else {
                            Text("**********")
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError, // Cambiar el color del contorno basado en el estado de error de contraseña
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (passwordError) Color.Red else MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = if (passwordError) Color.Red else Color.Gray
                    ),
                    singleLine = true
                )

                // Separador
                Spacer(modifier = Modifier.height(16.dp))

                // Texto de registro
                Text("¿No tienes un perfil?", color=MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, style =
                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))

                // Texto de creación de perfil
                Text("Crea uno, solo te llevara un instante", color=MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, style =
                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))

                // Separador
                Spacer(modifier = Modifier.height(30.dp))

                // Botones de registro e inicio de sesión
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

                            if (textValueUser.value.isEmpty() || textValuePassword.value.isEmpty()) {
                                // Mostrar error si alguno de los campos está vacío
                                setUserError(textValueUser.value.isEmpty())
                                setPasswordError(textValuePassword.value.isEmpty())
                                return@OutlinedButton
                            }

                            scope.launch {
                                try {
                                    val loginRequest = BodyLoginResponse(textValueUser.value, textValuePassword.value)
                                    val userResponse = RetrofitClientLogin.create().validateUser(loginRequest)
                                    if (userResponse.exist  && !textValueUser.value.isEmpty() || !textValuePassword.value.isEmpty()) {
                                        // El usuario existe, realiza la navegación
                                        //SharedPreferencesManager.saveLoggedInUser(LocalContext.current, textValueUser.value)
                                        onLoginSuccess()
                                    } else {
                                        setUserError(textValueUser.value.isEmpty())
                                        setPasswordError(textValuePassword.value.isEmpty())

                                        setUserError(true)
                                        setPasswordError(true)
                                        return@launch
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

                // Separador
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
