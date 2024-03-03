package com.example.utttilife.components.friend

import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resumeWithException
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class ChatViewModel : ViewModel() {
    // Lista de mensajes
    var messages = mutableStateListOf<ChatMessage>()
        private set

    // OkHttp Client
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    // Almacena el hilo de la comversacion actual
    private var currentThreadId: String? = null

    fun sendMessage(userMessage: String) {
        // Agrega el mensaje del usuario a la lista
        val userChatMessage = ChatMessage(userMessage, true)
        messages.add(userChatMessage)

        viewModelScope.launch {
            try {
                val botResponse = getBotResponse(userMessage, currentThreadId)
                messages.add(botResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getBotResponse(userMessage: String, threadId: String?): ChatMessage {
        val requestBody = JSONObject().apply {
            put("message", userMessage)
            put("thread_id", threadId)
        }.toString().toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())

        println("Mensaje a enviar: $userMessage")
        println("thread_id a enviar: $threadId")

        val request = Request.Builder()
            .url("https://camilonaffah.pythonanywhere.com/chat")
            .post(requestBody)
            .build()

        return suspendCancellableCoroutine { continuation ->
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody)
                    val botResponseMessage = jsonResponse.getString("response")
                    val newThreadId = jsonResponse.getString("thread_id")
                    currentThreadId = newThreadId // Actualiza el threadId actual
                    val botResponse = ChatMessage(botResponseMessage, false, newThreadId)
                    println("Bot Response - Message: $botResponseMessage - ThreadId: $newThreadId")
                    continuation.resume(botResponse) {
                        call.cancel()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
        }
    }
}

data class ChatMessage(val text: String, val isUser: Boolean, var threadId: String? = null)
