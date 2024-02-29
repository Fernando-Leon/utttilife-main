package com.example.utttilife.components.friend
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ChatViewModel : ViewModel() {
    // Lista de mensajes
    var messages = mutableStateListOf<ChatMessage>()
        private set

    fun sendMessage(userMessage: String) {
        // Agrega el mensaje del usuario a la lista
        val userChatMessage = ChatMessage(userMessage, true)
        messages.add(userChatMessage)

        // Simula obtener una respuesta del bot
        viewModelScope.launch {
            delay(1000) // Simula un retraso de red
            // Agrega la respuesta simulada del bot a la lista
            val botResponse = ChatMessage("Esto es una respuesta simulada a '${userChatMessage.text}'", false)
            messages.add(botResponse)
        }
    }
}

data class ChatMessage(val text: String, val isUser: Boolean)