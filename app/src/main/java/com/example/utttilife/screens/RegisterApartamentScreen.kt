package com.example.utttilife.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.utttilife.components.RegisterApartamentComponents.RegisterApartamentComponents
import com.example.utttilife.components.friend.ChatViewModel

@Preview
@Composable
fun RegisterApartamentScreen(viewModel: ChatViewModel = viewModel()) {


    val backgrondColor = Color(0x00FFFFFF)
    val scrollState= rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(0.dp, Color.White, shape = RoundedCornerShape(10.dp))
                .background(backgrondColor, RoundedCornerShape(10.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                item { RegisterApartamentComponents(onRegistrationSuccess = { }) }
            }
        }
    }
}

