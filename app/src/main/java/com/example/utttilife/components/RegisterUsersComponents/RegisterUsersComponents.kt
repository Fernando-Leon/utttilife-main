    package com.example.utttilife.screens

    import androidx.compose.foundation.layout.*
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.text.input.PasswordVisualTransformation
    import androidx.compose.ui.unit.dp
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import com.example.utttilife.data.Responses.ResponsesUsers.BodyUserRegisterResponse
    import com.example.utttilife.data.clients.RetrofitClientUserRegister
    import kotlinx.coroutines.launch

    // Importa el componente de Snackbar
    import androidx.compose.material3.SnackbarDuration

    import androidx.compose.material3.SnackbarHostState
    import androidx.compose.ui.text.style.TextOverflow
    import androidx.compose.ui.unit.sp

    @Composable
    fun RegisterComponent(
        onReisterSuccess: () -> Unit,
        onLoginSucess: () -> Unit,
        onTerminesAndConditioners: () -> Unit
    ) {

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val textValueUser = remember { mutableStateOf("") }
        val textValueName = remember { mutableStateOf("") }
        val textValueEmail = remember { mutableStateOf("") }
        val textValuePassword = remember { mutableStateOf("") }
        val textValuePasswordRepeat = remember { mutableStateOf("") }
        val textValuePhoneNumber = remember { mutableStateOf("") }
        val checkedState = remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Registro",
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
       )
        Spacer(modifier = Modifier.height(30.dp))

    // Campo de texto para el nombre de usuario
        OutlinedTextField(
            value = textValueUser.value,
            onValueChange = { textValueUser.value = it },
            placeholder = { Text(text = "Ej.Len123") },
            label = { Text(text = "Nombre usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

    // Campo de texto para el nombre
        OutlinedTextField(
            value = textValueName.value,
            onValueChange = { textValueName.value = it },
            placeholder = { Text(text = "Arturo Darinel Lopez Castillo") },
            label = { Text(text = "Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

    // Campo de texto para el correo electrónico
        OutlinedTextField(
            value = textValueEmail.value,
            onValueChange = { textValueEmail.value = it },
            placeholder = { Text(text = "Ej. arturdar16@gmail.com") },
            label = { Text(text = "Correo electronico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para el número de teléfono
        OutlinedTextField(
            value = textValuePhoneNumber.value,
            onValueChange = {    if (it.length <= 10) { // Asegurar que no se exceda el límite
                textValuePhoneNumber.value = it
            } },
            placeholder = { Text(text = "Ej.7731333534") },
            label = { Text(text = "Numero telefonico") },
            modifier = Modifier.fillMaxWidth(),

        )
        Spacer(modifier = Modifier.height(16.dp))

    // Campo de texto para la contraseña
        OutlinedTextField(
            value = textValuePassword.value,
            onValueChange = { textValuePassword.value = it },
            placeholder = { Text(text = "La contraseña debe tener minimo 8 caracteres") },
            label = { Text(text = "Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(16.dp))

    // Campo de texto para repetir la contraseña
        OutlinedTextField(
            value = textValuePasswordRepeat.value,
            onValueChange = { textValuePasswordRepeat.value = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "Confirmar ontraseña") },
            modifier = Modifier.fillMaxWidth()
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
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onTerminesAndConditioners()
                    }
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Alinea los elementos horizontalmente
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            OutlinedButton(
                onClick = {onLoginSucess()},
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Iniciar Sesión")
            }
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

                            val UsersRegisterResponse = RetrofitClientUserRegister.createUser().CreateUsers(response)


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
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
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