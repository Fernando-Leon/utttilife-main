package com.example.utttilife.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.utttilife.components.friend.ChatViewModel
import com.example.utttilife.components.friend.MessageView
import com.example.utttilife.components.friend.UserInput
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ConsultingScreen(viewModel: ChatViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 70.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "AMIGO",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier.weight(1f)) {

                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    MessageView(message =  "Hola como estas", isUserMessage = false)
                }

                items(viewModel.messages) { message ->
                    MessageView(message = message.text, isUserMessage = message.isUser)
                }
            }
            UserInput(onMessageSent = { viewModel.sendMessage(it) })
        }
    }
}