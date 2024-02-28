package com.example.utttilife.components.rents

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.utttilife.data.clients.DirectionsClient
import com.example.utttilife.data.services.DirectionsApiService
import com.example.utttilife.models.RouteResponse
import com.example.utttilife.navigation.BottomSheetRouter
import com.example.utttilife.navigation.Screen
import com.example.utttilife.screens.DepartmentsView
import com.example.utttilife.screens.ReviewsView
import com.example.utttilife.R.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
            .fillMaxWidth()
            .height(400.dp)
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
                    state = MarkerState(position = marker.position),  // Estado del marcador con la posición especificada
                    title = marker.title,  // Título del marcador
                    snippet = marker.snippet,  // Texto descriptivo del marcador
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

@Composable
fun SearchBar(){
    // Estado para almacenar el valor del texto en el campo de búsqueda
    val textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = textValue.value,  // Valor del campo de texto
        onValueChange = {
            textValue.value = it  // Actualiza el valor del campo de texto cuando cambia
        },
        leadingIcon = {
            // Icono que precede al campo de texto
            Icon(painter = painterResource(id = drawable.search), contentDescription = "")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(IntrinsicSize.Min)  // Altura mínima
            .clip(RoundedCornerShape(50))  // Forma redondeada para los bordes
            .background(colorResource(id = color.search_bar)),  // Color de fondo del campo de búsqueda
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),  // Opciones del teclado virtual
        keyboardActions = KeyboardActions(onSearch = {
            /* Define la acción de búsqueda aquí */  // Acción que se ejecuta cuando se presiona el botón de búsqueda en el teclado
        }),
        singleLine = true,  // Especifica que el campo de texto es de una sola línea
        placeholder = { Text(text = "Buscar renta") },  // Texto de marcador de posición
        shape = RoundedCornerShape(50)  // Forma redondeada para los bordes del campo de texto
    )
}

@Composable
fun DetailsDepartment(header: String, subHeader: String, onDirectionsClick: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        // Componente encabezado
        HeaderText(header)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(5.dp))
        // Component subencabezado
        SubHeaderText(subHeader)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(10.dp))
        // Botones de opción para acciones adicionales
        OptionButtons(onDirectionsClick = onDirectionsClick)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(5.dp))
        // Componente de transición que muestra diferentes vistas en función del estado actual
        Crossfade(targetState = BottomSheetRouter.showedScreen, label = "") {
                currentState ->
            // Evaluar el estado actual y mostrar la vista correspondiente
            when(currentState.value){
                is Screen.BottomSheetDepartmentScreen -> {
                    DepartmentsView()  // Vista de departamentos
                }
                is Screen.BottomSheetReviewsScreen -> {
                    ReviewsView()  // Vista de reseñas
                }
            }
        }
    }
}

@Composable
fun HeaderText(textValue: String){
    // Texto con estilo para encabezados
    Text(
        text = textValue,  // Valor del texto del encabezado
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,  // Grosor de la fuente semi-negrita
            fontSize = 20.sp,  // Tamaño de la fuente en escala independiente de píxeles (sp)
        )
    )
}

@Composable
fun SubHeaderText(textValue: String){
    // Texto con estilo para subencabezados
    Text(
        text = textValue,  // Valor del texto del subencabezado
        style = TextStyle(
            fontWeight = FontWeight.Light,  // Grosor de la fuente ligero
            fontSize = 20.sp,  // Tamaño de la fuente en escala independiente de píxeles (sp)
        )
    )
}

@Composable
fun OptionButtons(onDirectionsClick: () -> Unit){
    // Estado de desplazamiento para la fila de botones
    val scrollState = rememberScrollState()
    // Fila que contiene los botones de opción
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),  // Permite el desplazamiento horizontal
        horizontalArrangement = Arrangement.SpaceEvenly  // Distribución horizontal uniforme
    ) {
        Spacer(modifier = Modifier.width(5.dp))  // Espaciador
        // Botón genérico para mostrar los departamentos
        GenericOutlinedButton(
            name = "Departamentos",  // Nombre del botón
            painterResource = painterResource(id = drawable.home_work),  // Icono del botón
            backColor = Color.Transparent,  // Color de fondo transparente
            color = Color.Blue  // Color del texto del botón
        ) {
            BottomSheetRouter.navigateTo(Screen.BottomSheetDepartmentScreen)  // Navega a la pantalla de departamentos
        }
        Spacer(modifier = Modifier.width(5.dp))  // Espaciador
        // Botón genérico para mostrar las opiniones
        GenericOutlinedButton(
            name = "Opiniones",  // Nombre del botón
            painterResource = painterResource(id = drawable.reviews),  // Icono del botón
            backColor = Color.Transparent,  // Color de fondo transparente
            color = Color.Blue  // Color del texto del botón
        ) {
            BottomSheetRouter.navigateTo(Screen.BottomSheetReviewsScreen)  // Navega a la pantalla de opiniones
        }
        Spacer(modifier = Modifier.width(5.dp))  // Espaciador
        // Botón genérico para la función "Cómo llegar"
        GenericOutlinedButton(
            name = "Como llegar",  // Nombre del botón
            painterResource = painterResource(id = drawable.map),  // Icono del botón
            backColor = Color.Transparent,  // Color de fondo transparente
            color = Color.Blue  // Color del texto del botón
        ) {
            onDirectionsClick()  // Ejecuta la acción de "Cómo llegar" proporcionada por el parámetro
        }
    }
}


@Composable
fun GenericOutlinedButton(
    name: String,  // Texto del botón
    painterResource: Painter,  // Icono del botón
    backColor: Color,  // Color de fondo del botón
    color: Color,  // Color del texto del botón
    onClick: () -> Unit  // Acción que se ejecuta al hacer clic en el botón
){
    // Botón con borde delgado y esquinas redondeadas
    OutlinedButton(
        border = BorderStroke(1.dp, Color.Blue),  // Borde del botón
        shape = RoundedCornerShape(50),  // Forma redondeada para las esquinas
        onClick = onClick,  // Acción de clic del botón
        colors = ButtonDefaults.buttonColors(  // Colores personalizados para el botón
            contentColor = color,  // Color del texto del botón
            containerColor = backColor  // Color de fondo del botón
        ),
    ) {
        // Icono del botón
        Icon(painter = painterResource, contentDescription = "")
        // Espaciador horizontal
        Spacer(modifier = Modifier.width(2.dp))
        // Texto del botón
        Text(text = name)
    }
}

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