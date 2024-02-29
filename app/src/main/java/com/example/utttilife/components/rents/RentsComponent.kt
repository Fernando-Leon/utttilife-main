package com.example.utttilife.components.rents

import android.Manifest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.utttilife.models.RouteResponse
import com.example.utttilife.R.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

data class MapMarker(val position: LatLng, val title: String, val snippet: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Map() {
    // Estado para controlar el estado del BottomSheet
    val sheetState = rememberModalBottomSheetState()
    // Estado para controlar la visibilidad del BottomSheet
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    // Estado para almacenar el marcador seleccionado
    var selectedMarker by remember { mutableStateOf<MapMarker?>(null) }
    // Estado para almacenar la ubicación actual
    var currentLocation by remember { mutableStateOf("") }
    // Valor para realizar operaciones asíncronas
    val scope = rememberCoroutineScope()
    // Estado para almacenar la ruta entre dos puntos
    var poly by remember { mutableStateOf<RouteResponse?>(null) }

    // Contexto actual
    val context = LocalContext.current
    // Lista de marcadores en el mapa
    val markers = listOf(
        MapMarker(LatLng(20.00846578560767, -99.34343022797654), "UTTT", "Universidad Tecnológica de Tula-Tepeji"),
        MapMarker(LatLng(20.010636633917777, -99.34230840358448), "Renta 1", "Renta cerca de la universidad"),
        MapMarker(LatLng(20.004550343505713, -99.34475737477435), "Renta 2", "Renta un poco lejos de la universidad"),
        MapMarker(LatLng(19.99850205210948, -99.348568437716), "Renta 3", "Renta a 10 minutos de la universidad"),
    )

    // Estado de la posición de la cámara del mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markers.first().position, 16f)
    }
    // Estilo del mapa en formato JSON
    val mapStyleJson = remember {
        context.resources.openRawResource(raw.map_style).bufferedReader().use { it.readText() }
    }

    // Estado de los permisos de ubicación
    val permissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    // Solicitar permiso de ubicación
    LaunchedEffect(Unit){
        permissionState.launchPermissionRequest()
    }

    // Obtener la ubicación del usuario solo si se tiene el permiso
    LaunchedEffect(Unit) {
        if (permissionState.status.isGranted){
            scope.launch {
                val result = getUserLocation(context)
                if (result != null){
                    currentLocation = "${result.longitude},${result.latitude}"
                }
            }
        }
    }

    // Función para calcular la ruta entre la ubicación actual y un marcador
    val onDirectionsClick = {
        currentLocation.let { _ ->
            selectedMarker?.let { marker ->
                scope.launch {
                    val origin = currentLocation
                    val destination = marker.position.longitude.toString() + "," + marker.position.latitude.toString()
                    poly = getRoute(origin, destination)
                }
            }
        }
    }

    // Componente MapBox que contiene el mapa
    MapBox(
        markers = markers,
        cameraPositionState = cameraPositionState,
        mapStyleJson = mapStyleJson,
        poly = poly,
        onMarkerClick = { marker ->
            selectedMarker = marker
            showBottomSheet = true
            poly = null
        }
    )

    // Mostrar el BottomSheet al hacer click en un marcador
    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false }
        ) {
            selectedMarker?.let {
                DetailsDepartment(header = it.title, subHeader = it.snippet) {
                    onDirectionsClick()
                }
            }
        }
    }
}