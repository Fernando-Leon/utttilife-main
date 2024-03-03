package com.example.utttilife.components.rents

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.utttilife.data.clients.DirectionsClient
import com.example.utttilife.data.services.BuildingsApiService
import com.example.utttilife.data.services.DirectionsApiService
import com.example.utttilife.models.RouteResponse
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
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

suspend fun getCurrentLocation(context: Context): Location? {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    if (!isGPSEnabled) {
        return null
    }

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return null
    }

    return suspendCancellableCoroutine { cont ->
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                // Se detienen las actualizaciones de ubicación para evitar múltiples llamadas
                fusedLocationProviderClient.removeLocationUpdates(this)
                // Se reanuda la corrutina con la ubicación actual (la más reciente de la lista)
                cont.resume(result.lastLocation)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}

suspend fun obtainMarkers(service: BuildingsApiService): List<MapMarker> {
    val response = service.getBuildings()
    return if (response.isSuccessful) {
        val buildingsList = response.body() ?: emptyList()
        buildingsList.map { building ->
            MapMarker(
                id = building._id,
                coordinates = listOf(building.coordinates[0], building.coordinates[1]),
                name = building.name,
                address = building.address,
                opinions = building.opinions,
                tenant = building.tenant
            )
        }
    } else {
        println("Occurred an error during the API call")
        emptyList()
    }
}