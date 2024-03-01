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

    val checkedState = remember { mutableStateOf(false) }

    val buttonColor = Color(0xFF02A787) // Color del botón 'Registrarme' y del borde del botón 'Iniciar Sesión'
    val textColorButton = Color(0xFFF4F7F6)
    val buttonShape = RoundedCornerShape(4.dp) // Forma redondeada para el borde y el botón
    val backgrondColor = Color(0x40FFFFFF)
    val textFielColor = Color(0xABFFFFFF)
    val textBienvenidosColor= Color(0xFF3F3E3E)
    val textColorPreguntaPerfil = Color(0xFF2E302F)
    val buttonColorsRegistrarme = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF02A787)
    )


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
                .background(backgrondColor, RoundedCornerShape(10.dp))


            ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(56.dp))
                Text("Bienvenido", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), color=textBienvenidosColor)
                Spacer(modifier = Modifier.height(1.dp))
                Text("Inicio de sesion", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), color=Color.White)
                Spacer(modifier = Modifier.height(36.dp))
                val textValueUser= remember{
                    mutableStateOf("")
                }
                TextField(
                    value = textValueUser.value,
                    onValueChange = { textValueUser.value=it},
                    placeholder = { Text("Usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(textFielColor)
                        .border(2.dp, Color.White, shape = RoundedCornerShape(15.dp)),

                )

                Spacer(modifier = Modifier.height(16.dp))
                val textValuePassword = remember{
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
                        .background(textFielColor)
                        .border(2.dp, Color.White, shape = RoundedCornerShape(15.dp)),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("No tienes un perfil?, crea uno, solo te llevara un instante", color=textColorPreguntaPerfil, textAlign = TextAlign.Center, style =
                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = { isChecked -> checkedState.value = isChecked },
                        modifier = Modifier.padding(end = 8.dp) // Ajusta el espaciado según sea necesario


                    )
                    Text("Aceptar términos y condiciones de servicio", color=Color.White, textAlign = TextAlign.Center, style =
                    MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Alinea los elementos horizontalmente
                ) {
                    Button(
                        onClick = { onRegisterClicked() }, // Llama al callback cuando se hace clic en el botón
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

                    OutlinedButton(

                        onClick = {

                            // Comprueba que los términos y condiciones están aceptados
                            if (!checkedState.value) {
                                // Mostrar mensaje de error sobre los términos y condiciones
                                return@OutlinedButton
                            }
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
                        shape = buttonShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = textColorButton
                        ),
                        border = BorderStroke(1.dp, buttonColor)
                    ) {
                        Text("Iniciar Sesión")
                    }
                }
            }
        }
    }
}

