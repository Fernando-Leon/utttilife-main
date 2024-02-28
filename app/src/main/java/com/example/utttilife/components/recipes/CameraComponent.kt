package com.example.utttilife.components.recipes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.utttilife.R
import java.io.File
import java.io.IOException
import java.util.concurrent.Executor

//Camera component
@Composable
fun Camera(
    cameraController: LifecycleCameraController,
    lifeCycle: LifecycleOwner,
    modifier: Modifier
){
    cameraController.bindToLifecycle(lifeCycle)
    //Create a view in the component
    AndroidView(modifier = modifier, factory = {context ->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT,
            )
        }
        previewView.controller = cameraController
        previewView
    })
}


//Component to open camara
@Composable
fun OpenCamera(navController: NavController) {
    IconButton(
        onClick = {
            navController.navigate("camera")
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.camera),
            contentDescription = "Abrir Camara",
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.size(24.dp)
        )
    }
}

//Take a picture
public fun takePicture(navController: NavController, cameraController: LifecycleCameraController, executor: Executor, context: Context, onImageCaptured: (Bitmap) -> Unit){
    // Default name to temporal file generate
    val file = File.createTempFile("receta", ".jpg", context.externalCacheDir)
    // Save options to output of the captured image
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback{
        // In case image save success
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = outputFileResults.savedUri ?: return
            try {
                val source = ImageDecoder.createSource(context.contentResolver, savedUri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                onImageCaptured(bitmap)
                navController.popBackStack()
            } catch (e: IOException) {
                println("Ocurrio un error en: ${e.message}")
            }
        }

        override fun onError(exception: ImageCaptureException) {

        }
    })
}