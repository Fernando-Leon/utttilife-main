package com.example.utttilife.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utttilife.components.friend.ChatViewModel
import com.example.utttilife.components.friend.MessageView
import com.example.utttilife.components.friend.UserInput
import androidx.lifecycle.viewmodel.compose.viewModel

// Screen to view chat
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
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "AMIGO",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = "ILIFE",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)) {

                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    MessageView(message =  "Hola, ¿Como estas?\nSoy tu amigo ilife\nSi necesitas platicar aqui estoy 😊.", isUserMessage = false)
                }

                items(viewModel.messages) { message ->
                    MessageView(message = message.text, isUserMessage = message.isUser)
                }
            }
            UserInput(onMessageSent = { viewModel.sendMessage(it) })
        }
    }
}