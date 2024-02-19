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

//Funcion para tomar foto
public fun takePicture(navController: NavController, cameraController: LifecycleCameraController, executor: Executor, context: Context, onImageCaptured: (Bitmap) -> Unit){
    //Nombre por defecto que se le pondra a el archivo temporal generado
    val file = File.createTempFile("receta", ".jpg", context.externalCacheDir)
    //Guardar las opciones de salida de la imagen capturada
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback{
        //En caso de que la imagen se guarde correctamente pasa esto
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            //Variable que guarda la ruta del archivo generado
            val savedUri = outputFileResults.savedUri ?: return
            try {
                //Ruta del archivo selleccionado
                val source = ImageDecoder.createSource(context.contentResolver, savedUri)
                //Decodificamos la imagen
                val bitmap = ImageDecoder.decodeBitmap(source)
                //Pasamos la imagen capturada
                onImageCaptured(bitmap)
                //Regresar a la pantalla anterior despues de tomar la foto
                navController.popBackStack()
            } catch (e: IOException) {
                println("Ocurrio un error en: ${e.message}")
            }
        }

        override fun onError(exception: ImageCaptureException) {

        }
    })
}

//Componente para la camara
@Composable
fun Camera(
    cameraController: LifecycleCameraController,
    lifeCycle: LifecycleOwner,
    modifier: Modifier
){
    cameraController.bindToLifecycle(lifeCycle)
    //Creamos una vista dentro del componente
    AndroidView(modifier = modifier, factory = {context ->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT,
            )
        }
        //Previsualización de la camara
        previewView.controller = cameraController
        previewView
    })
}

//Componente para abrir camara
@Composable
fun OpenCamera(navController: NavController) {
    //Icon button para ejecutar la acción
    IconButton(
        onClick = {
            navController.navigate("camera")
        }
    ) {
        //Icono del botón
        Icon(
            painter = painterResource(id = R.drawable.camera),
            contentDescription = "Abrir Camara",
            tint = Color.Blue,
            modifier = Modifier.size(30.dp)
        )
    }
}

//Componente para seleccionar imagen desde galeria
@Composable
fun ImageSelector(selectedImage: MutableState<Bitmap?>) {
    //Almacenamos el contexto de la aplicación
    val context = LocalContext.current
    //Obtener el content resolver
    val contentResolver: ContentResolver = context.contentResolver
    //Recordamos el resultado de la actividad para obtener la imagen seleccionada de la galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        //Actualizamos el valor con la imagen seleccionada
        selectedImage.value = uri?.let { decodeUri(contentResolver, it) }
    }
    //Icon button para acceder a la galeria
    IconButton(onClick = { galleryLauncher.launch("image/*") }) {
        Icon(
            painter = painterResource(id = R.drawable.add_image),
            tint = Color.Blue,
            contentDescription = "Agregar imagen",
            modifier = Modifier.size(30.dp)
        )
    }
}

//Componente para almacenar el contenido de nuestra app en un lazy column
@Composable
fun ContentScroll(
    selectedImage: MutableState<Bitmap?>,
    response: String,
    modifier: Modifier = Modifier
) {
    //Items que tendra el lazy column
    LazyColumn(modifier = modifier) {
        item { Spacer(Modifier.height(50.dp)) }
        item { SelectedImageView(selectedImage) }
        item { ObtainedResponse(response) }
    }
}

//Componente que nos mostrara la imagen seleccionada
@Composable
fun SelectedImageView(selectedImage: MutableState<Bitmap?>) {
    selectedImage.value?.let { bitmap ->
        //Box para almacenar la imagen
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

//Componente para mostrar la respuesta de OpenAi
@Composable
fun ObtainedResponse(response: String){
    //Box para almacenar la respuesta
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

//Componente encargado para el envio de la imagen
@Composable
fun ContentSend(selectedImageBitmap: Bitmap?, response: MutableState<String>) {
    val scope = rememberCoroutineScope()

    IconButton(onClick = {
        //Relizamos la petición a OpenAi mediante Courotines
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

//Función que obtiene hace la petición a la api de OpenAi
suspend fun getResponse(selectedImageBitmap: Bitmap): String = withContext(Dispatchers.IO) {
    //Definimos el api Key
    val apiKey = "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"
    //Url del servicio
    val url = "https://api.openai.com/v1/chat/completions"
    //Convertimos la imagen seleccionada a Base64
    val outputStream = ByteArrayOutputStream()
    selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray())
    //Definimos el cuerpo de la petición en formato JSON
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
    //Creamos nuestra petición
    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer $apiKey")
        .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
        .build()
    //Establecemos el cliente y un tiempo de espera
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    //Suspendemos la ejecución hasta que se reciba una respuesta
    suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : Callback {
            //En caso de que falle se hace lo siguiente
            override fun onFailure(call: Call, e: IOException) {
                println("Error en la llamada HTTP: ${e.message}")
                e.printStackTrace()
                continuation.resumeWith(Result.failure(e))
            }
            //Si se obtiene una respuesta se hace lo siguiente
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        println("Error en la respuesta HTTP: Código de estado = ${response.code}, Mensaje = ${response.message}")

                        val responseBody = response.body?.string()
                        println("Cuerpo de la respuesta: $responseBody")

                        continuation.resumeWith(Result.failure(IOException("Error: ${response.message}")))
                    } else {
                        //Obtenemos el cuerpo de la respuesta
                        val body = response.body?.string()
                        //Creamos un JSONonject a partir del cuerpo
                        val jsonObject = JSONObject(body)

                        if (jsonObject.has("choices")) {
                            //Obtenemos el array llamado choices
                            val choicesArray = jsonObject.getJSONArray("choices")
                            if (choicesArray.length() > 0) {
                                //Obtenemos el primer objeto JSON
                                val firstChoice = choicesArray.getJSONObject(0)
                                if (firstChoice.has("message") && firstChoice.getJSONObject("message").has("content")) {
                                    //Obtenemos el contenido de content
                                    val content = firstChoice.getJSONObject("message").getString("content")
                                    //Reanudamos la ejecución
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

//Decodifica un objeto URI en uno Bitmap
private fun decodeUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        // Abrimos un flujo de entrada desde la URI utilizando el ContentResolver.
        val inputStream = contentResolver.openInputStream(uri)
        // Decodificamos el flujo de entrada en un objeto Bitmap utilizando BitmapFactory.
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

//Clase que almacena la imagen
class MainViewModel : ViewModel() {
    // Estado de la imagen seleccionada.
    val selectedImage: MutableState<Bitmap?> = mutableStateOf(null)

    // Función para actualizar la imagen seleccionada.
    fun onImageCaptured(bitmap: Bitmap) {
        selectedImage.value = bitmap
    }
}