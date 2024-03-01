package com.example.utttilife.components.GaleryViewComponents

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.utttilife.R


@Composable
fun GalleryView() {
    // Estado para almacenar las imágenes seleccionadas, correctamente recordado
    val selectedImages = remember { mutableStateOf(listOf<Bitmap>()) }
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        // Procesa las URIs seleccionadas
        val bitmaps = uris?.mapNotNull { uri ->
            decodeUriImages(contentResolver, uri)
        }
        if (bitmaps != null) {
            selectedImages.value = bitmaps
        }
    }

    // Mostrar las imágenes seleccionadas
    ShowSelectedImages(selectedImages = selectedImages)

    // Coloca el IconButton aquí si es parte de MultipleImageSelector
    IconButton(onClick = { galleryLauncher.launch("image/*") }) {
        Icon(
            painter = painterResource(id = R.drawable.add_photo),
            contentDescription = "Agregar imagen",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(30.dp)
        )
    }
}


@Composable
fun ShowSelectedImages(selectedImages: State<List<Bitmap>>) {
    // LazyRow para desplazamiento horizontal de las imágenes seleccionadas
    LazyRow {
        items(selectedImages.value) { image ->
            Image(bitmap = image.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .padding(4.dp) // Añade un poco de espacio alrededor de la imagen
                    .size(100.dp) // Tamaño de la vista previa de la imagen
            )
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