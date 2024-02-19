package com.example.utttilife.views.recipes

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.utttilife.components.recipes.Camera
import com.example.utttilife.components.recipes.ContentScroll
import com.example.utttilife.components.recipes.ContentSend
import com.example.utttilife.components.recipes.ImageSelector
import com.example.utttilife.components.recipes.MainViewModel
import com.example.utttilife.components.recipes.OpenCamera
import com.example.utttilife.components.recipes.takePicture
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

//Componente que organiza los botones en un Scaffold
@Composable
fun LayoutContent(navController: NavController, viewModel: MainViewModel) {
    //Variable que almacena la respuesta
    val response = remember { mutableStateOf("1.- Selecciona o toma una imagen.\n2.- Presiona el botón de enviar.\n3.- A cocinar!.") }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OpenCamera(navController)
                    ImageSelector(viewModel.selectedImage)
                    ContentSend(viewModel.selectedImage.value, response)
                }
            }
        }
    ) { innerPadding ->
        ContentScroll(viewModel.selectedImage, response.value, Modifier.padding(innerPadding))
    }
}

// Componente para abrir la cámara
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraImage(navController: NavController, onImageCaptured: (Bitmap) -> Unit, context: Context) {
    // Obtenemos el estado del permiso para la cámara
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    // Creamos un controlador de cámara que recuerda su estado de ciclo de vida
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    // Obtenemos el ciclo de vida actual
    val lifeCycle = LocalLifecycleOwner.current

    // Solicitamos el permiso de cámara
    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Si el permiso está concedido, mostramos la vista de cámara y el botón para tomar la foto
        if (permissionState.status.isGranted) {
            Camera(cameraController, lifeCycle, modifier = Modifier.padding(50.dp))
            FloatingActionButton(
                onClick = {
                    val executor = ContextCompat.getMainExecutor(context)
                    takePicture(navController, cameraController, executor, context, onImageCaptured)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Tomar foto")
            }
        } else {
            // Si el permiso está denegado, mostramos un mensaje indicando que el permiso fue denegado
            Text(text = "Permiso denegado")
        }
    }
}
