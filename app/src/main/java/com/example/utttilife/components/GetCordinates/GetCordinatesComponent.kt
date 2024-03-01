        package com.example.utttilife.components.GetCordinates

        import androidx.compose.foundation.layout.Row
        import androidx.compose.foundation.layout.Spacer
        import androidx.compose.foundation.layout.fillMaxWidth
        import androidx.compose.foundation.layout.padding
        import androidx.compose.material3.OutlinedTextField
        import androidx.compose.material3.Text
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.LaunchedEffect
        import androidx.compose.runtime.mutableStateOf
        import androidx.compose.runtime.remember
        import androidx.compose.runtime.rememberCoroutineScope
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.platform.LocalContext
        import androidx.compose.ui.tooling.preview.Preview
        import androidx.compose.ui.unit.dp
        import com.google.accompanist.permissions.ExperimentalPermissionsApi
        import com.google.accompanist.permissions.isGranted
        import com.google.accompanist.permissions.rememberPermissionState
        import kotlinx.coroutines.launch
        import android.Manifest
        import android.content.Context
        import android.location.Location
        import android.location.LocationManager
        import androidx.compose.foundation.background
        import androidx.compose.foundation.layout.Arrangement
        import androidx.compose.foundation.shape.RoundedCornerShape
        import kotlinx.coroutines.Dispatchers
        import kotlinx.coroutines.withContext
        import androidx.compose.material3.Button
        import androidx.compose.material3.ButtonDefaults
        import androidx.compose.material3.MaterialTheme


        @OptIn(ExperimentalPermissionsApi::class)
        @Composable
        fun GetCordinatesComponent() {
            // Variables para almacenar las coordenadas como texto editable
            val latitudeText = remember { mutableStateOf("0.0") }
            val longitudeText = remember { mutableStateOf("0.0") }

            // Estado de los permisos de ubicación
            val permissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

            // Variable para la Coroutina
            val scope = rememberCoroutineScope()

            // Variable para obtener el contexto
            val context = LocalContext.current

            //variable para el color de el boton
            val colorButtom = Color(0xFF02A787)

            // Solicitar permiso de ubicación
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                OutlinedTextField(
                    value = latitudeText.value,
                    onValueChange = { latitudeText.value = it },
                    label = { Text("Latitud") },
                    modifier = Modifier.weight(.4f),
                    enabled = false
                )

                Spacer(modifier = Modifier.padding(horizontal = 2.dp))

                OutlinedTextField(
                    value = longitudeText.value,
                    onValueChange = { longitudeText.value = it },
                    label = { Text("Longitud") },
                    modifier = Modifier.weight(.4f),
                    enabled = false
                )


            }
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Button(
                onClick = {
                    scope.launch {
                        if (permissionState.status.isGranted) {
                            val location = withContext(Dispatchers.IO) { getUserLocation(context) }
                            if (location != null) {
                                latitudeText.value = location.latitude.toString()
                                longitudeText.value = location.longitude.toString()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)

            ){
                Text(text = "Obtener Ubicacion", color = Color.White)
                    }
        }


      fun getUserLocation(context: Context): Location? {
            // Obtener el LocationManager del sistema
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers: List<String> = locationManager.getProviders(true)
            var bestLocation: Location? = null

            try {
                for (provider in providers) {
                    val l: Location = locationManager.getLastKnownLocation(provider) ?: continue
                    if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                        bestLocation = l
                    }
                }
            } catch (e: SecurityException) {
                // Log the error or request permissions if necessary
            }

            return bestLocation
        }

        @Preview(showBackground = true)
        @Composable
        fun PreviewGetCordinatesComponent() {
            GetCordinatesComponent()
        }
