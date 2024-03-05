package com.example.utttilife.components.GaleryViewComponents

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.utttilife.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GalleryView(onImageUpload: (List<String>) -> Unit) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val selectedImages = remember { mutableStateOf(listOf<Uri>()) }

    val uploadedImageUrls = remember { mutableStateListOf<String>() }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        // Guarda las URIs seleccionadas
        if (uris != null) {
            selectedImages.value = uris
        }
    }

    Column {
        ShowSelectedImages(selectedImages = selectedImages, contentResolver)

        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Seleccionar Imágenes")
        }

        Button(onClick = {
            selectedImages.value.forEach { uri ->
                CoroutineScope(Dispatchers.IO).launch {
                    uploadImage(context, uri) { success, imageUrl ->
                        if (success && imageUrl != null) {
                            // La subida fue exitosa, y imageUrl contiene la URL de la imagen.
                            Log.d("UploadSuccess", "Imagen subida: $imageUrl")
                            uploadedImageUrls.add(imageUrl)
                            onImageUpload(uploadedImageUrls)

                        } else {
                            // La subida falló.
                            Log.d("UploadError", "La subida de la imagen falló.")
                        }
                    }
                }
            }
        }) {
            Text("Subir Imágenes")
        }
    }
}

@Composable
fun ShowSelectedImages(selectedImages: State<List<Uri>>, contentResolver: ContentResolver) {
    LazyRow {
        items(selectedImages.value) { uri ->
            val bitmap = decodeUriImages(contentResolver, uri)
            bitmap?.let {
                Image(bitmap = it.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .padding(4.dp)
                        .size(100.dp)
                )
            }
        }
    }
}

fun decodeUriImages(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream).also {
            inputStream?.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun uploadImage(context: Context, imageUri: Uri, onComplete: (Boolean, String?) -> Unit) {
    MediaManager.get().upload(imageUri)
        .callback(object : UploadCallback {
            override fun onStart(requestId: String) {
                // Este método se llama cuando comienza la subida.
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                // Este método se llama para notificar el progreso de la subida.
            }

            override fun onSuccess(requestId: String, resultData: Map<Any?, Any?>) {
                // Este método se llama cuando la subida ha sido exitosa.
                // Aquí es donde puedes obtener la URL de la imagen.
                val url = resultData["url"] as? String
                onComplete(true, url) // Pasar true y la URL al callback.
            }

            override fun onError(requestId: String, error: ErrorInfo) {
                // Este método se llama si la subida falla.
                onComplete(false, null)
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
                // Este método se llama si la subida necesita ser reprogramada.
                onComplete(false, null)
            }
        }).dispatch()
}