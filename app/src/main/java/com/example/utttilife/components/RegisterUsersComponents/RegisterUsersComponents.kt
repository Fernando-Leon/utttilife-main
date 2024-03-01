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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.utttilife.data.Responses.ResponsesLogin.BodyLoginResponse
import com.example.utttilife.data.Responses.ResponsesUsers.BodyUserRegisterResponse
import com.example.utttilife.data.Responses.ResponsesUsers.UsersRegisterResponse
import com.example.utttilife.data.clients.RetrofitClientLogin
import com.example.utttilife.data.clients.RetrofitClientUserRegister
import kotlinx.coroutines.launch

// Importa el componente de Snackbar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

@Composable
fun RegisterComponent(onReisterSuccess: () -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }


    val scope = rememberCoroutineScope()

    val buttonColor =
        Color(0xFF02A787) // Color del botón 'Registrarme' y del borde del botón 'Iniciar Sesión'
    val textColorButton = Color(0xFFF4F7F6)
    val buttonShape = RoundedCornerShape(4.dp) // Forma redondeada para el borde y el botón
    val backgrondColor = Color(0x00FFFFFF)

    val textValueUser = remember { mutableStateOf("") }
    val textValueName = remember { mutableStateOf("") }
    val textValueEmail = remember { mutableStateOf("") }
    val textValuePassword = remember { mutableStateOf("") }
    val textValuePasswordRepeat = remember { mutableStateOf("") }
    val textValuePhoneNumber = remember { mutableStateOf("") }
    val checkedState = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(56.dp))
    Text("Registro", style = MaterialTheme.typography.headlineLarge)
    Spacer(modifier = Modifier.height(36.dp))

// Campo de texto para el nombre de usuario
    TextField(
        value = textValueUser.value,
        onValueChange = { textValueUser.value = it },
        placeholder = { Text("Crea un nombre de usuario") },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp))
    )
    Spacer(modifier = Modifier.height(16.dp))

// Campo de texto para el nombre
    TextField(
        value = textValueName.value,
        onValueChange = { textValueName.value = it },
        placeholder = { Text("Nombre Completo") },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp))
    )
    Spacer(modifier = Modifier.height(16.dp))

// Campo de texto para el correo electrónico
    TextField(
        value = textValueEmail.value,
        onValueChange = { textValueEmail.value = it },
        placeholder = { Text("Correo Electronico") },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp))
    )

    Spacer(modifier = Modifier.height(16.dp))

// Campo de texto para la contraseña
    TextField(
        value = textValuePassword.value,
        onValueChange = { textValuePassword.value = it },
        placeholder = { Text("Contraseña") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp)),
    )
    Spacer(modifier = Modifier.height(16.dp))

// Campo de texto para repetir la contraseña
    TextField(
        value = textValuePasswordRepeat.value,
        onValueChange = { textValuePasswordRepeat.value = it },
        placeholder = { Text("Repite la Contraseña") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp)),
    )
    Spacer(modifier = Modifier.height(16.dp))

// Campo de texto para el número de teléfono
    TextField(
        value = textValuePhoneNumber.value,
        onValueChange = { textValuePhoneNumber.value = it },
        placeholder = { Text("Numero de telefono") },
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
            checked = checkedState.value,
            onCheckedChange = { isChecked -> checkedState.value = isChecked },
            modifier = Modifier.padding(end = 8.dp) // Ajusta el espaciado según sea necesario
        )
        Text(
            "Aceptar términos y condiciones de servicio",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )

    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly // Alinea los elementos horizontalmente
    ) {
        Button(
            onClick = {
                // Comprueba que los términos y condiciones están aceptados
                if (!checkedState.value) {
                    // Mostrar mensaje de error sobre los términos y condiciones
                    return@Button
                }
                // Comprueba que los campos no estén vacíos
                if (textValueUser.value.isEmpty() || textValuePassword.value.isEmpty()) {
                    // Mostrar mensaje de error sobre campos vacíos
                    return@Button
                }
                // Comprueba si el correo electrónico es válido
                if (!isValidEmail(textValueEmail.value)) {
                    // Mostrar mensaje de error sobre correo electrónico inválido
                    return@Button
                }
                // Comprueba si la contraseña y su repetición coinciden
                if (textValuePassword.value != textValuePasswordRepeat.value) {
                    // Mostrar mensaje de error sobre contraseñas no coincidentes
                    return@Button
                }
                // Comprueba si el número de teléfono tiene la longitud adecuada
                if (textValuePhoneNumber.value.length != 10) {
                    // Mostrar mensaje de error sobre la longitud del número de teléfono
                    return@Button
                }
                scope.launch {
                    try {
                        // Realiza la solicitud de registro
                        val response = BodyUserRegisterResponse(
                            textValueUser.value,
                            textValueName.value,
                            textValuePassword.value,
                            textValueEmail.value,
                            textValuePhoneNumber.value
                        )

                        val UsersRegisterResponse =
                            RetrofitClientUserRegister.createUser().CreateUsers(response)


                        // Verifica si la respuesta es exitosa
                        if (!UsersRegisterResponse.successfull) {
                            // Realiza la navegación si el registro fue exitoso
                            onReisterSuccess()


                            // Muestra el Snackbar de éxito
                            snackbarHostState.showSnackbar(
                                message = "Usuario registrado con éxito :)",
                                duration = SnackbarDuration.Short
                            )

                        } else {
                            snackbarHostState.showSnackbar(
                                message = "Usuario registrado sin éxito :(",
                                duration = SnackbarDuration.Short
                            )
                        }
                    } catch (e: Exception) {
                        // Manejar el error, por ejemplo, mostrando un mensaje al usuario

                    }
                }
            },
            modifier = Modifier.weight(1f),
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.White
            )
        ) {
            Text("Registrarme")
        }


    }
    Spacer(modifier = Modifier.width(8.dp))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {


        // Finalmente, coloca el SnackbarHost en tu Box
        SnackbarHost(hostState = snackbarHostState)
    }


}

// Función para verificar si un correo electrónico es válido
fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return emailRegex.toRegex().matches(email)
}