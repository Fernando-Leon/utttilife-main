package com.example.utttilife.components.rents

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.utttilife.data.clients.DirectionsClient
import com.example.utttilife.data.services.DirectionsApiService
import com.example.utttilife.models.RouteResponse
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun getRoute(origin: String, destination: String): RouteResponse {
    // La función se suspende hasta que se obtiene la respuesta de la solicitud de la ruta
    return suspendCoroutine { continuation ->
        // Se lanza una corrutina en el hilo de fondo para realizar la solicitud de manera asíncrona
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Se realiza la llamada a la API de direcciones para obtener la ruta
                val call = DirectionsClient.makeDirectionsService()
                    .create(DirectionsApiService::class.java)
                    .getDirections("5b3ce3597851110001cf624861c3ba57e965475987a39b21d2d35231", origin, destination)

                // Se verifica si la llamada fue exitosa
                if (call.isSuccessful) {
                    // Se obtiene la respuesta de la llamada
                    val directionsResponse = call.body()
                    // Se verifica si la respuesta no es null
                    if (directionsResponse != null) {
                        // Se resuelve la corrutina con la respuesta obtenida
                        continuation.resume(directionsResponse)
                    } else {
                        // Si la respuesta es null, se lanza una excepción
                        continuation.resumeWithException(NullPointerException("Response body is null"))
                    }
                } else {
                    // Si la llamada no fue exitosa, se lanza una excepción
                    continuation.resumeWithException(Exception("Unsuccessful response"))
                }
            } catch (e: Exception) {
                // Se capturan posibles excepciones y se resuelven en la corrutina
                continuation.resumeWithException(e)
            }
        }
    }
}

suspend fun getUserLocation(context: Context): android.location.Location? {
    // Obtiene una instancia del proveedor de servicios de ubicación
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    // Obtiene una instancia del administrador de ubicación del sistema
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Verifica si el GPS está habilitado
    val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.GPS_PROVIDER)

    // Si el GPS no está habilitado, devuelve null
    if (!isGPSEnabled) {
        return null
    }

    // Verifica si se otorgó el permiso necesario para acceder a la ubicación precisa del dispositivo
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return null
    }

    // Se suspende la función hasta que se obtenga la ubicación del usuario
    return suspendCancellableCoroutine { cont ->
        // Se utiliza el método lastLocation del proveedor de servicios de ubicación fusionada para obtener la última ubicación conocida
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            // Verifica si la tarea se completó con éxito
            if (task.isSuccessful) {
                // Obtiene la ubicación del usuario del resultado de la tarea
                val androidLocation = task.result
                // Se reanuda la corrutina con la ubicación obtenida
                cont.resume(androidLocation)
            } else {
                // Si la tarea no se completó con éxito, se reanuda la corrutina con null
                cont.resume(null)
            }
        }
    }
}