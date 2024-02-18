package com.example.utttilife.components.recipes

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.utttilife.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.Base64
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

public fun takePicture(navController: NavController, cameraController: LifecycleCameraController, executor: Executor, context: Context, onImageCaptured: (Bitmap) -> Unit){
    val file = File.createTempFile("receta", ".jpg", context.externalCacheDir)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback{
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


@Composable
fun Camera(
    cameraController: LifecycleCameraController,
    lifeCycle: LifecycleOwner,
    modifier: Modifier
){
    cameraController.bindToLifecycle(lifeCycle)
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
            tint = Color.Blue,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun ImageSelector(selectedImage: MutableState<Bitmap?>) {
    val context = LocalContext.current

    val contentResolver: ContentResolver = context.contentResolver

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImage.value = uri?.let { decodeUri(contentResolver, it) }
    }

    IconButton(onClick = { galleryLauncher.launch("image/*") }) {
        Icon(
            painter = painterResource(id = R.drawable.add_image), tint = Color.Blue, contentDescription = "Agregar imagen",
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun ContentScroll(
    selectedImage: MutableState<Bitmap?>,
    response: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item { Spacer(Modifier.height(50.dp)) }
        item { SelectedImageView(selectedImage) }
        item { ObtainedResponse(response) }
    }
}

@Composable
fun SelectedImageView(selectedImage: MutableState<Bitmap?>) {
    selectedImage.value?.let { bitmap ->
        Box(
            modifier = Modifier
                .padding(16.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = BitmapPainter(bitmap.asImageBitmap()),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat()) // Mantiene la relación de aspecto de la imagen
            )
        }
    }
}

@Composable
fun ObtainedResponse(response: String){
    Box(
        modifier = Modifier
            .padding(16.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            text = response,
            style = MaterialTheme.typography.bodyLarge.copy (
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun Content(selectedImageBitmap: Bitmap?, response: MutableState<String>) {
    val scope = rememberCoroutineScope()

    IconButton(onClick = {
        try {
            selectedImageBitmap?.let {
                scope.launch {
                    response.value = getResponse(it)
                }
            }
        } catch (e: Exception){
            println("Ocurrio un error en: ${e.message}")
        }
    })
    {
        Icon(
            Icons.Filled.Send, tint = Color.Blue, contentDescription = "Enviar",
            modifier = Modifier.size(30.dp)
        )
    }
}

suspend fun getResponse(selectedImageBitmap: Bitmap): String = withContext(Dispatchers.IO) {
    val apiKey = "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"
    val url = "https://api.openai.com/v1/chat/completions"

    val outputStream = ByteArrayOutputStream()
    selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray())

    val requestBody = """
        {
            "model": "gpt-4-vision-preview",
            "messages": [
                {
                    "role": "user",
                    "content": [
                        {
                            "type": "text",
                            "text": "Identifica los ingredientes de la siguiente imagen y genera una receta en base a los ingredientes especificados, solo devuelveme el nombre de la receta y el como prepararla y trata de ser lo mas claro posible"
                        },
                        {
                            "type": "image_url",
                            "image_url": {
                                "url": "data:image/jpeg;base64,$base64Image"
                            }
                        }
                    ]
                }
            ],
            "max_tokens": 800
        }
    """.trimIndent()

    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer $apiKey")
        .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
        .build()

    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Error en la llamada HTTP: ${e.message}")
                e.printStackTrace()
                continuation.resumeWith(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        println("Error en la respuesta HTTP: Código de estado = ${response.code}, Mensaje = ${response.message}")

                        val responseBody = response.body?.string()
                        println("Cuerpo de la respuesta: $responseBody")

                        continuation.resumeWith(Result.failure(IOException("Error: ${response.message}")))
                    } else {
                        val body = response.body?.string()
                        val jsonObject = JSONObject(body)

                        if (jsonObject.has("choices")) {
                            val choicesArray = jsonObject.getJSONArray("choices")
                            if (choicesArray.length() > 0) {
                                val firstChoice = choicesArray.getJSONObject(0)
                                if (firstChoice.has("message") && firstChoice.getJSONObject("message").has("content")) {
                                    val content = firstChoice.getJSONObject("message").getString("content")
                                    continuation.resume(content)
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}

private fun decodeUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

class MainViewModel : ViewModel() {
    // Estado de la imagen seleccionada.
    val selectedImage: MutableState<Bitmap?> = mutableStateOf(null)

    // Función para actualizar la imagen seleccionada.
    fun onImageCaptured(bitmap: Bitmap) {
        selectedImage.value = bitmap
    }
}