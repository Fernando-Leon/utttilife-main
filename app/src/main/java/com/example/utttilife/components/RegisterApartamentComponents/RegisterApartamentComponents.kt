        package com.example.utttilife.components.RegisterApartamentComponents

        import androidx.compose.foundation.layout.Arrangement
        import androidx.compose.foundation.layout.Box
        import androidx.compose.foundation.layout.Row
        import androidx.compose.foundation.layout.Spacer
        import androidx.compose.foundation.layout.fillMaxWidth
        import androidx.compose.foundation.layout.height
        import androidx.compose.foundation.layout.width
        import androidx.compose.material3.Button
        import androidx.compose.material3.Checkbox
        import androidx.compose.material3.MaterialTheme
        import androidx.compose.material3.OutlinedTextField
        import androidx.compose.material3.SnackbarDuration
        import androidx.compose.material3.SnackbarHost
        import androidx.compose.material3.SnackbarHostState
        import androidx.compose.material3.Text
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.mutableStateOf
        import androidx.compose.runtime.remember
        import androidx.compose.runtime.rememberCoroutineScope
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.unit.dp
        import com.example.utttilife.components.GaleryViewComponents.GalleryView
        import com.example.utttilife.components.GetCordinates.GetCordinatesComponent
        import com.example.utttilife.data.Responses.ResponsesRents.ApartamentRegisterRequest
        import com.example.utttilife.data.clients.ApartamentsRegisterClients
        import kotlinx.coroutines.launch
        import org.chromium.base.Log


        @Composable
        fun RegisterApartamentComponents(  onRegistrationSuccess: () -> Unit){

            val coordinates = remember { mutableStateOf(listOf<Double>()) }
            val imageUrls = remember { mutableStateOf(listOf<String>()) }

            var selectedOption = remember { mutableStateOf("Casa") }
            // Demás estados del formulario...

            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()



            Spacer(modifier = Modifier.height(56.dp))
            Text("Registro de departamento", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(36.dp))
            val textValueNameApartament= remember{
                mutableStateOf("")
            }
            OutlinedTextField(
                value = textValueNameApartament.value,
                onValueChange = { textValueNameApartament.value=it},
                label = { Text("Nombre de el Edificio") },
                placeholder = { Text("Nombre de tu edificio/ departamento") },
                modifier = Modifier
                    .fillMaxWidth()

            )
            Spacer(modifier = Modifier.height(16.dp))
            val textValueDireccion= remember{
                mutableStateOf("")
            }
            OutlinedTextField(
                value = textValueDireccion.value,
                onValueChange = { textValueDireccion.value=it},
                label = { Text("Direccion") },
                placeholder = { Text("Ej.Cda de Argentina s/n") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Usar GetCordinatesComponent con callbacks para actualizar los estados
            GetCordinatesComponent { newCoordinates ->
                coordinates.value = newCoordinates
            }
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(56.dp))
            Text("Tipo de vivienda", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(36.dp))

            val options = listOf("Casa", "Apartamento")

            // RadioGroup con opciones "Casa" y "Apartamento"
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEach { option ->
                    Checkbox(
                        checked = option == selectedOption.value,
                        onCheckedChange = { isChecked -> if (isChecked) selectedOption.value = option }
                    )
                    Text(
                        text = option,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val textValueDescription= remember{
                mutableStateOf("")
            }
            OutlinedTextField(
                value = textValueDescription.value,
                onValueChange = { textValueDescription.value=it},
                label = { Text("Descripcion") },
                placeholder = { Text("Ej.Apartamento con 3 habitaciones, todos los servicios") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            val textValueCost= remember{
                mutableStateOf("")
            }
            OutlinedTextField(
                value = textValueCost.value,
                onValueChange = { textValueCost.value =it},
                label = { Text("Costo Mensual") },
                placeholder = { Text("Ej. $1500 mensuales por persona") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16  .dp))

            Spacer(modifier = Modifier.height(16.dp))
            val textValueRules= remember{
                mutableStateOf("")
            }
            OutlinedTextField(
                value = textValueRules.value,
                onValueChange = { textValueRules.value =it},
                label = { Text("Reglas") },
                placeholder = { Text("Ej. No se permiten mascotas, no fiestas, etc.") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16  .dp))

            GalleryView { urls ->
                imageUrls.value = urls
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Lógica para validar el formulario
            val isFormComplete = remember(
                coordinates.value,
                textValueNameApartament.value,
                textValueDireccion.value,
                textValueDescription.value,
                textValueCost.value,
                textValueRules.value,
                imageUrls.value
            ) {
                textValueNameApartament.value.isNotBlank() &&
                        textValueDireccion.value.isNotBlank() &&
                        coordinates.value.isNotEmpty() &&
                        imageUrls.value.isNotEmpty() &&
                        textValueDescription.value.isNotBlank() &&
                        textValueCost.value.isNotBlank() &&
                        textValueRules.value.isNotBlank()
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center // Alinea los elementos horizontalmente
            ) {
                Button(
                    onClick = {
                        if (isFormComplete) {
                            scope.launch {
                                try {
                                    // Suponiendo que tienes una instancia de tu servicio Retrofit llamada apartamentRegisterService
                                    val request = ApartamentRegisterRequest(
                                        name = textValueNameApartament.value,
                                        address = textValueDireccion.value,
                                        coordinates = coordinates.value,
                                        type = selectedOption.value, // Aquí capturas el tipo de vivienda
                                        description = textValueDescription.value,
                                        cost = textValueCost.value.toInt(),
                                        rules = textValueRules.value,
                                        ImageUrl = imageUrls.value
                                    )

                                    val response = ApartamentsRegisterClients.ApartamentsRegisterCli().RegisterApartaments(request)

                                    if (!response.success) {
                                        snackbarHostState.showSnackbar(
                                            message = "Apartamento registrado con éxito",
                                            duration = SnackbarDuration.Short
                                        )
                                        Log.d("UploadSuccess","Apartamento registrado con éxito")
                                        onRegistrationSuccess()
                                    } else {
                                        snackbarHostState.showSnackbar(
                                            message = "Error al registrar el apartamento",
                                            duration = SnackbarDuration.Short
                                        )
                                        Log.d("UploadSuccess","registro de apartamento sin éxito")
                                    }
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar(
                                        message = "Error de red o del servidor",
                                        duration = SnackbarDuration.Short
                                    )
                                    Log.d("UploadError","registro de apartamento sin éxito: ", e )
                                }
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Por favor, completa todos los campos correctamente",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    enabled = isFormComplete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Registrar Vivienda")
                }
                Box(  modifier= Modifier.fillMaxWidth()){
                    SnackbarHost(hostState = snackbarHostState)
                    Spacer(modifier = Modifier.width(8.dp))
                }


            }
        }