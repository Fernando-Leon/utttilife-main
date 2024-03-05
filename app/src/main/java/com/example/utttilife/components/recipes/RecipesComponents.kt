package com.example.utttilife.components.recipes

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.utttilife.components.indicators.IndeterminateCircularIndicator
import kotlinx.coroutines.Dispatchers
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
import java.io.IOException
import java.util.Base64
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


// Lazy column to content app
@Composable
fun ContentScroll(
    selectedImage: MutableState<Bitmap?>,
    response: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item { Spacer(Modifier.height(50.dp)) }
        item { StepsToCreateRecipe() }
        item { SelectedImageView(selectedImage) }
        item { ObtainedResponse(response) }
    }
}

//Get response IA
@Composable
fun ObtainedResponse(response: String){
    Box(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
            .fillMaxWidth()
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
            )
        )
    }
}

@Composable
fun StepsToCreateRecipe() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Recetas",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Crea recetas apartir de imagenes con inteligencia artificial"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Pasos: ",
                fontWeight = FontWeight.SemiBold,
                fontSize = 19.sp,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "1: Selecciona o captura una imagen")
            Text(text = "2: Envia la imagen")
            Text(text = "3: Espera y a cocinar")

        }
    }
}

//Request for OpenIA Api - Model GPT 4
suspend fun getResponse(selectedImageBitmap: Bitmap): String = withContext(Dispatchers.IO) {
    val apiKey = "API_KEY"
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
                            "text": "A partir de la imagen adjunta, que muestra diversos alimentos disponibles, por favor genera una receta de comida que incluya los siguientes elementos: Nombre de la receta: Un título creativo y descriptivo basado en los alimentos visibles en la imagen. Ingredientes: Lista detallada de los ingredientes necesarios para la receta, también deben incluir los condimentos aunque no se muestren en la imagen. Pasos de la preparació Instrucciones paso a paso para preparar la receta, asegurándose de que sean claras y fáciles de seguir. Información nutricional: Proporciona una estimación de las calorías, grasas, proteínas y carbohidratos por porción de la receta generada. Nota: La imagen adjunta se utiliza como referencia para identificar los alimentos disponibles y sugerir una receta que los utilice de manera eficiente y creativa, los elementos de la imagen deben ser los primarios."
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
            "max_tokens": 3000
        }
    """.trimIndent()
    // Request
    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer $apiKey")
        .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
        .build()
    // Establish time of charge
    val client = OkHttpClient.Builder()
        .connectTimeout(180, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(180, TimeUnit.SECONDS)
        .build()

    // Suspend response async
    suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : Callback {
            // In case failed
            override fun onFailure(call: Call, e: IOException) {
                println("Error en la llamada HTTP: ${e.message}")
                e.printStackTrace()
                continuation.resumeWith(Result.failure(e))
            }
            // if get response
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        println("Error en la respuesta HTTP: Código de estado = ${response.code}, Mensaje = ${response.message}")

                        val responseBody = response.body?.string()
                        println("Cuerpo de la respuesta: $responseBody")

                        continuation.resumeWith(Result.failure(IOException("Error: ${response.message}")))
                    } else {
                        // Get response body
                        val body = response.body?.string()
                        // create to JSONonject for the body
                        val jsonObject = JSONObject(body)

                        if (jsonObject.has("choices")) {
                            // Get choices array
                            val choicesArray = jsonObject.getJSONArray("choices")
                            if (choicesArray.length() > 0) {
                                // Get first JSON object
                                val firstChoice = choicesArray.getJSONObject(0)
                                if (firstChoice.has("message") && firstChoice.getJSONObject("message").has("content")) {
                                    // Get content
                                    val content = firstChoice.getJSONObject("message").getString("content")
                                    // Resume flow
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

//Decoder object URI to Bitmap
fun decodeUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        // Open flow the inputs
        val inputStream = contentResolver.openInputStream(uri)
        // Decode flow utilizing BitmapFactory
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Class for save image
class MainViewModel : ViewModel() {
    // State of selected image
    val selectedImage: MutableState<Bitmap?> = mutableStateOf(null)

    // Refresh selected image
    fun onImageCaptured(bitmap: Bitmap) {
        selectedImage.value = bitmap
    }
}