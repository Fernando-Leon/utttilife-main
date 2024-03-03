package com.example.utttilife.components.rents

import android.Manifest
import androidx.compose.foundation.layout.Column
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
import androidx.navigation.NavHostController
import com.example.utttilife.models.RouteResponse
import com.example.utttilife.R.*
import com.example.utttilife.data.Responses.ResponsesRents.Opinion
import com.example.utttilife.data.Responses.ResponsesRents.Tenant
import com.example.utttilife.data.clients.BuildingsClient
import com.example.utttilife.data.services.BuildingsApiService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

data class MapMarker(val id: String, val coordinates: List<Double>, val name: String, val address: String, val opinions: List<Opinion>, val tenant: Tenant)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Map(navController: NavHostController) {
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
    // Variables de busqueda
    var searchText = remember { mutableStateOf("") }
    var foundMarker by remember { mutableStateOf<MapMarker?>(null) }
    // Contexto actual
    val context = LocalContext.current
    // Lista de marcadores en el mapa
    var markers by remember { mutableStateOf<List<MapMarker>?>(null) }
    // Estado de la posición de la cámara del mapa
    val defaultPosition = LatLng(20.009354068554533, -99.34279123027005)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultPosition, 16f)
    }
    // Estilo del mapa en formato JSON
    val mapStyleJson = remember {
        context.resources.openRawResource(raw.map_style).bufferedReader().use { it.readText() }
    }

    val service = BuildingsClient.makeBuildingsService()

    // Estado de los permisos de ubicación
    val permissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    // Solicitar permiso de ubicación
    LaunchedEffect(Unit){
        permissionState.launchPermissionRequest()
    }

    LaunchedEffect(Unit){
        val fetchedMarkers = obtainMarkers(service.create(BuildingsApiService::class.java))
        markers = fetchedMarkers
    }

    // Obtener la ubicación del usuario solo si se tiene el permiso
    LaunchedEffect(Unit) {
        if (permissionState.status.isGranted){
            scope.launch {
                val result = getCurrentLocation(context)
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
                    val destination = marker.coordinates[1].toString() + "," + marker.coordinates[0].toString()
                    poly = getRoute(origin, destination)
                }
            }
        }
    }

    // Componente MapBox que contiene el mapa
    markers?.let {
        Column {
            SearchBar(searchText = searchText, onSearch = { query ->
                // Buscar el marcador por nombre
                foundMarker = markers?.find { it.name.contains(query, ignoreCase = true) }
                // Si se encuentra un marcador, actualiza la posición de la cámara
                foundMarker?.let { marker ->
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(marker.coordinates[0],marker.coordinates[1]), 16f)
                }
            })
            MapBox(
                markers = it,
                cameraPositionState = cameraPositionState,
                mapStyleJson = mapStyleJson,
                poly = poly,
                onMarkerClick = { marker ->
                    selectedMarker = marker
                    showBottomSheet = true
                    poly = null
                }
            )
        }
    }

    // Mostrar el BottomSheet al hacer click en un marcador
    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false }
        ) {
            selectedMarker?.let {
                DetailsDepartment(navController, coordinates = it.coordinates, name = it.name, address = it.address, opinions = it.opinions, tenant = it.tenant) {
                    onDirectionsClick()
                }
            }
        }
    }
}