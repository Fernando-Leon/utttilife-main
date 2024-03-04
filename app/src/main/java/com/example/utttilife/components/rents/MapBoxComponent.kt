package com.example.utttilife.components.rents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.utttilife.models.RouteResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import kotlinx.coroutines.launch

@Composable
fun MapBox(
    markers: List<MapMarker>,  // Lista de marcadores para mostrar en el mapa
    cameraPositionState: CameraPositionState,  // Estado de la posición de la cámara del mapa
    mapStyleJson: String,  // Estilo del mapa en formato JSON
    poly: RouteResponse?,  // Ruta entre dos puntos
    onMarkerClick: (MapMarker) -> Unit  // Función de devolución de llamada cuando se hace clic en un marcador
) {
    // Contenedor principal que aloja el mapa
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(400.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ,RoundedCornerShape(10.dp)
            )
    ) {
        // Mapa de Google
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,  // Establece el estado de la posición de la cámara
            properties = MapProperties(
                mapStyleOptions = MapStyleOptions(mapStyleJson)  // Establece el estilo del mapa
            )
        ) {
            // Recorrer la lista de marcadores y colocarlos en el mapa
            markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.coordinates[0],marker.coordinates[1])),  // Estado del marcador con la posición especificada
                    title = marker.name,  // Título del marcador
                    snippet = marker.address,  // Texto descriptivo del marcador
                    onClick = {
                        onMarkerClick(marker)  // Llama a la función de devolución de llamada cuando se hace clic en el marcador
                        true  // Indica que se ha manejado el clic en el marcador
                    }
                )
            }
            // Si hay una ruta disponible (poly), dibujarla en el mapa
            poly?.features?.firstOrNull()?.geometry?.coordinates?.let { coordinates ->
                val points = coordinates.map { LatLng(it[1], it[0]) }  // Convertir las coordenadas en puntos LatLng
                Polyline(
                    points = points,  // Puntos que componen la ruta
                    color = Color.Blue,  // Color de la ruta
                    width = 10f  // Ancho de la línea
                )
                // Ajustar la posición de la cámara para que se ajuste al tamaño de la ruta
                val bounds = LatLngBounds.Builder().apply {
                    points.forEach { include(it) }  // Incluir todos los puntos en los límites del mapa
                }.build()

                // Obtener el ancho y la altura de la pantalla
                val displayMetrics = LocalContext.current.resources.displayMetrics
                val width = displayMetrics.widthPixels
                val height = displayMetrics.heightPixels

                // Crear una actualización de la cámara que ajuste los límites de los puntos
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, 100)
                val scope = rememberCoroutineScope()
                scope.launch {
                    cameraPositionState.animate(cameraUpdate)  // Animar la cámara para mostrar toda la ruta en pantalla
                }
            }
        }
    }
}